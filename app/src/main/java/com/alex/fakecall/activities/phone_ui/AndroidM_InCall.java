package com.alex.fakecall.activities.phone_ui;


import android.widget.Chronometer;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;
import com.alex.fakecall.models.Call;

import butterknife.BindView;
import butterknife.OnClick;

public class AndroidM_InCall extends BaseActivity {
    @BindView(R.id.tvDuration)
    Chronometer tvDuration;

    @BindView(R.id.tvName)
    TextView tvName;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_incall_android6;
    }

    @Override
    protected void setUp() {
        tvDuration.start();
        Call call = (Call)getIntent().getSerializableExtra(Call.KEY);
        tvName.setText(call.name);
    }

    @OnClick(R.id.btnEndcall)
    void endCall(){
        tvDuration.stop();
        this.finish();
    }
}
