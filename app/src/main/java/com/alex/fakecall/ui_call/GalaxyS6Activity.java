package com.alex.fakecall.ui_call;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.fakecall.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GalaxyS6Activity extends BaseCallActivity {
    @BindView(R.id.top)
    RelativeLayout lyTop;

    @BindView(R.id.bottom)
    RelativeLayout lyBottom;

    @BindView(R.id.tvIncoming)
    TextView tvIncoming;

    @BindView(R.id.lyChronometer)
    View lyChronometer;

    @BindView(R.id.lyBtnAnswer)
    View lyBtnAnswer;

    @BindView(R.id.lyBtnInCall)
    View lyBtnInCall;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_galaxy_s6;
    }

    @Override
    protected void configureForInComing() {
        lyTop.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 0.5f));
        lyBottom.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 0.5f));

        tvIncoming.setVisibility(View.VISIBLE);
        lyChronometer.setVisibility(View.GONE);
        lyBtnAnswer.setVisibility(View.VISIBLE);
        lyBtnInCall.setVisibility(View.GONE);
    }

    @Override
    protected void configureForInCall() {
        lyTop.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 0.4f));
        lyBottom.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 0.6f));

        tvIncoming.setVisibility(View.GONE);
        lyChronometer.setVisibility(View.VISIBLE);
        lyBtnAnswer.setVisibility(View.GONE);
        lyBtnInCall.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btnAccept)
    void onBtnAcceptClick() {
        onAnswerCall();
    }

    @OnClick(R.id.btnReject)
    void onBtnRejectClick() {
        onMissedCall();
    }

}
