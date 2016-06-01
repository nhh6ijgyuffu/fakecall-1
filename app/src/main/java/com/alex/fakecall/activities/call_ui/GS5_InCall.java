package com.alex.fakecall.activities.call_ui;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.Converter;
import com.alex.fakecall.utils.PhoneUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class GS5_InCall extends BaseActivity implements SensorEventListener {
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvFakeNumber)
    TextView tvFakeNumber;

    private SensorManager sensorManager;
    private Sensor proximity;

    private boolean isScreenOff = false;

    private int totalSecond = 0;
    private Handler handler = new Handler();

    private Runnable countUpTask = new Runnable() {
        @Override
        public void run() {
            tvDuration.setText(Converter.secondToMMSS(totalSecond));
            totalSecond++;

            //Every second
            handler.postDelayed(countUpTask, 1000);
        }
    };

    private Call callInstance;

    @Override
    public int getLayoutResource() {
        return R.layout.in_call_gs5;
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(countUpTask);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        callInstance = (Call) getIntent().getSerializableExtra("call");

        if (callInstance.getCallerName() == null) {
            tvFakeNumber.setText(callInstance.getCallerNumber());
        } else {
            tvFakeNumber.setText(callInstance.getCallerName());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] == 0) {
                if (!isScreenOff)
                    turnOffScr();
            } else {
                if (isScreenOff)
                    turnOnScr();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    void turnOffScr() {
        isScreenOff = true;

    }

    void turnOnScr() {
        isScreenOff = false;

    }

    @OnClick(R.id.btnEndcall)
    void onClickEndCall() {
        callInstance.setDuration(totalSecond);
        PhoneUtils.addCallLog(this, callInstance);
        finish();
    }
}
