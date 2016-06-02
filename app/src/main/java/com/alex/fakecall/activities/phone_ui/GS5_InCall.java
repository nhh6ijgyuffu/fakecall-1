package com.alex.fakecall.activities.phone_ui;


import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;

import butterknife.BindView;

public class GS5_InCall extends BaseActivity{
    @BindView(R.id.tvDuration)
    TextView tvDuration;

    @BindView(R.id.tvFakeNumber)
    TextView tvFakeNumber;

    @Override
    public int getLayoutResource() {
        return R.layout.in_call_gs5;
    }
}
