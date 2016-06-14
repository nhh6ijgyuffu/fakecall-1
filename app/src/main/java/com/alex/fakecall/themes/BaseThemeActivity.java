package com.alex.fakecall.themes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;
import com.alex.fakecall.controllers.AudioController;
import com.alex.fakecall.controllers.AudioController.PlayerTag;
import com.alex.fakecall.controllers.VibrationController;
import com.alex.fakecall.controllers.WakeUpController;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.CallLogUtils;
import com.alex.fakecall.utils.Utils;
import com.alex.fakecall.views.CountableChronometer;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;


public abstract class BaseThemeActivity extends BaseActivity implements SensorEventListener {
    @BindView(R.id.chronometer)
    CountableChronometer chronometer;

    @BindView(R.id.mask)
    RelativeLayout maskLayout;

    @Nullable
    @BindView(R.id.tvName)
    TextView tvName;

    @Nullable
    @BindView(R.id.tvNumber)
    TextView tvNumber;

    private boolean isInCall = false;
    private boolean isPreview;
    protected Call mCall;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private boolean isScreenOff = false;

    private MyHandler mHandler;
    private static final int MAX_TIME_TO_MISSED_CALL = 60 * 1000;

    private NotificationManager mNotifyManager;
    private static final int NOTIFY_ID_INCOMING = Integer.MAX_VALUE - 1;
    private static final int NOTIFY_ID_ONGOING = Integer.MAX_VALUE;

    private AudioManager mAudioManager;

    protected abstract void configureForInCall();

    protected abstract void configureForInComing();

    private static class MyHandler extends Handler {
        public static final int HANDLE_MSG_MISSED = 1;
        public static final int HANDLE_MSG_END_CALL = 2;

        private WeakReference<BaseThemeActivity> mWeakAct;

        public MyHandler(BaseThemeActivity act) {
            mWeakAct = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseThemeActivity act = mWeakAct.get();
            if (act == null) return;
            switch (msg.what) {
                case HANDLE_MSG_END_CALL:
                    act.onEndCall();
                    break;
                case HANDLE_MSG_MISSED:
                    act.onMissedCall();
                    break;
            }
        }
    }

    @Override
    protected void onSetUp() {
        mCall = getIntent().getParcelableExtra(Call.TAG);
        if (mCall != null) {
            if (tvName != null)
                tvName.setText(mCall.getDisplayName());
            if (tvNumber != null)
                tvNumber.setText(mCall.getNumber());
        }

        isPreview = getIntent().getBooleanExtra("isPreview", false);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mHandler = new MyHandler(this);

        configureForInComing();
        mHandler.sendEmptyMessageDelayed(MyHandler.HANDLE_MSG_MISSED, MAX_TIME_TO_MISSED_CALL);

        AudioController.getInstance().startPlaying(PlayerTag.RINGTONE,
                Uri.parse(mCall.getRingtoneUri()), true);

        if (mCall.isVibrate()) {
            VibrationController.getInstance().vibrate(true);
        }

        WakeUpController.getInstance().wakeUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mProximity);
    }

    protected void answerCall() {
        isInCall = true;
        configureForInCall();
        mHandler.removeMessages(MyHandler.HANDLE_MSG_MISSED);

        long callDuration = mCall.getCallDuration();

        if (callDuration != 0) {
            mHandler.sendEmptyMessageDelayed(MyHandler.HANDLE_MSG_END_CALL, callDuration);
        }

        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        mAudioManager.setSpeakerphoneOn(false);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        AudioController.getInstance().stopPlaying(PlayerTag.RINGTONE);
        VibrationController.getInstance().cancelAll();

        if (mCall.getVoiceUri() != null) {
            AudioController.getInstance().startPlaying(PlayerTag.VOICE,
                    Uri.parse(mCall.getVoiceUri()), true);
        }
    }

    protected void onMissedCall() {
        if (!isPreview) {
            CallLogUtils.writeMissedCall(mCall);
            showMissedNotification();
        }
        doWorksAfterEnd();
        finish();
    }

    private void showMissedNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        builder.setDefaults(5);
        builder.setSmallIcon(R.drawable.ic_missed_call_white);
        builder.setTicker("Missed call from " + mCall.getDisplayName());
        builder.setContentTitle("Missed call");
        builder.setContentText(mCall.getDisplayName());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(805306368);
        intent.setType(CallLog.Calls.CONTENT_TYPE);

        Bitmap bm = Utils.decodeResource(this, R.drawable.avatar);
        if (bm != null)
            builder.setLargeIcon(bm);

        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        mNotifyManager.notify(10, builder.build());
    }

    @Optional
    @OnClick(R.id.btnEndCall)
    protected void onEndCall() {
        if (!isPreview) {
            CallLogUtils.writeIncomingCall(mCall, chronometer.getDuration());
        }
        doWorksAfterEnd();
        finish();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isInCall) return;
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] == 0) {
                if (isScreenOff) return;
                screenOff();
            } else {
                if (!isScreenOff) return;
                screenOn();
            }
        }
    }

    private void doWorksAfterEnd() {
        mHandler.removeMessages(MyHandler.HANDLE_MSG_MISSED);
        mHandler.removeMessages(MyHandler.HANDLE_MSG_END_CALL);
        mNotifyManager.cancel(NOTIFY_ID_INCOMING);
        mNotifyManager.cancel(NOTIFY_ID_ONGOING);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        AudioController.getInstance().stopAllPlaying();
        VibrationController.getInstance().cancelAll();
        WakeUpController.getInstance().reset();
    }

    protected void showIncomingNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        prepareNotification(builder);
        builder.setContentText("Incoming");
        mNotifyManager.notify(NOTIFY_ID_INCOMING, builder.build());
    }

    protected void showInCallNotification() {
        mNotifyManager.cancel(NOTIFY_ID_INCOMING);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        prepareNotification(builder);
        builder.setContentText("Ongoing");
        builder.setUsesChronometer(true);
        mNotifyManager.notify(NOTIFY_ID_ONGOING, builder.build());
    }

    private void prepareNotification(NotificationCompat.Builder builder) {
        builder.setAutoCancel(false);
        builder.setOnlyAlertOnce(false);
        builder.setOngoing(true);
        builder.setDefaults(32);

        if (mCall != null) {
            builder.setTicker(mCall.getDisplayName());
            builder.setContentTitle(mCall.getDisplayName());
        }

        Intent intent = new Intent(this, getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setSmallIcon(R.drawable.ic_notification_small);

        Bitmap bm = Utils.decodeResource(this, R.drawable.avatar);
        if (bm != null)
            builder.setLargeIcon(bm);
    }

    private void screenOff() {
        isScreenOff = true;
        try {
            if (maskLayout != null)
                maskLayout.setVisibility(View.VISIBLE);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attributes);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void screenOn() {
        isScreenOff = false;
        try {
            if (maskLayout != null)
                maskLayout.setVisibility(View.GONE);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.flags &= -1025;
            getWindow().setAttributes(attributes);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!isInCall) {
                    return true;
                }
                onEndCall();
                return true;
            case KeyEvent.KEYCODE_CALL:
                answerCall();
                return true;
            case KeyEvent.KEYCODE_ENDCALL:
                onEndCall();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
