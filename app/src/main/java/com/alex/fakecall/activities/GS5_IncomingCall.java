package com.alex.fakecall.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.CallLog;
import android.view.MotionEvent;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.PhoneUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;


public class GS5_IncomingCall extends BaseActivity {
    @BindView(R.id.lyArrow)
    View lyArrow;

    private Call callInstance;

    private static final int TIME_TO_HANG_UP = 25 * 1000;

    private CountDownTimer countDownTimer;

    @Override
    public int getLayoutResource() {
        return R.layout.incoming_call_gs5;
    }

    @Override
    protected void onStart() {
        super.onStart();

        callInstance = new Call();
        callInstance.setCallerName("Obama");
        callInstance.setCallerNumber("+84919456002");

        countDownTimer = new CountDownTimer(TIME_TO_HANG_UP, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onRejectCall();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @OnTouch({R.id.btnAccept, R.id.btnReject})
    boolean onTouch(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            lyArrow.setVisibility(View.INVISIBLE);
        } else if (action == MotionEvent.ACTION_UP) {
            lyArrow.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @OnClick(R.id.btnAccept)
    void onAcceptCall() {
        Intent intent = new Intent(this, GS5_InCall.class);
        callInstance.setType(CallLog.Calls.INCOMING_TYPE);
        intent.putExtra("call", callInstance);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnReject)
    void onRejectCall() {
        callInstance.setType(CallLog.Calls.MISSED_TYPE);
        PhoneUtils.addCallLog(this, callInstance);
        finish();
    }
}
