package com.alex.fakecall.activities;


import android.os.SystemClock;
import android.text.format.DateUtils;
import android.widget.Chronometer;

import com.alex.fakecall.R;

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
