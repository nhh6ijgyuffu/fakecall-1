package com.alex.fakecall.activities.phone_ui;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;


public class GS5_IncomingCall extends BaseActivity {
    @BindView(R.id.lyArrow)
    View lyArrow;

    @Override
    public int getLayoutResource() {
        return R.layout.incoming_call_gs5;
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
        startActivity(intent);
        finish();
    }
}
