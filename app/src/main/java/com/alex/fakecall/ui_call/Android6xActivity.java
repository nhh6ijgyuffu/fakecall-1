package com.alex.fakecall.ui_call;


import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.views.Android44xAnimation;
import com.alex.fakecall.views.CallAnimOnTriggerListener;

import butterknife.BindView;

public class Android6xActivity extends BaseCallActivity implements CallAnimOnTriggerListener {
    @BindView(R.id.callAnimation)
    Android44xAnimation callAnimation;

    @BindView(R.id.btnHold)
    View btnHold;

    @BindView(R.id.tvIncoming)
    View tvIncoming;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_android_6x;
    }

    @Override
    protected void configureForInCall() {
        btnHold.setVisibility(View.VISIBLE);
        callAnimation.setVisibility(View.GONE);
        tvIncoming.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        showInCallNotification();
    }

    @Override
    protected void configureForInComing() {
        btnHold.setVisibility(View.GONE);
        callAnimation.setVisibility(View.VISIBLE);
        tvIncoming.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.GONE);
        callAnimation.start();
        callAnimation.setOnTriggerListener(this);
        showIncomingNotification();
    }

    @Override
    public void onTrigger(int whatToTrigger) {
        switch (whatToTrigger) {
            case CallAnimOnTriggerListener.TRIGGER_ANSWER:
                onAnswerCall();
                break;
            case CallAnimOnTriggerListener.TRIGGER_DECLINE:
                onMissedCall();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        callAnimation.stop();
        super.onDestroy();
    }
}
