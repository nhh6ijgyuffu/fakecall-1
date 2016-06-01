package com.alex.fakecall.activities.call_ui;


import android.os.SystemClock;
import android.widget.Chronometer;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AndroidL_InCall extends BaseActivity {
    @BindView(R.id.tvDuration)
    Chronometer tvDuration;

    @Override
    public int getLayoutResource() {
        return R.layout.incall_androidl;
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvDuration.start();
    }

    @OnClick(R.id.btnEndcall)
    void endCall(){
        tvDuration.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - tvDuration.getBase();
        this.finish();
    }
}
