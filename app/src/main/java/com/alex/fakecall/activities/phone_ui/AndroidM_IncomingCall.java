package com.alex.fakecall.activities.phone_ui;


import android.content.ClipData;
import android.content.Intent;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.BaseActivity;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.views.AnimatedImageView;

import butterknife.BindView;

public class AndroidM_IncomingCall extends BaseActivity {
    @BindView(R.id.btnReceiveCall)
    ImageView btnReceiveCall;

    @BindView(R.id.circle)
    View circle;

    @BindView(R.id.btnAccept)
    ImageView btnAccept;

    @BindView(R.id.btnReject)
    ImageView btnReject;

    @BindView(R.id.btnSms)
    ImageView btnSms;

    @BindView(R.id.callAnim)
    AnimatedImageView callAnim;

    @BindView(R.id.tvName)
    TextView tvName;

    private Call call;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_incoming_call_android6;
    }

    @Override
    protected void setUp() {
        btnReceiveCall.setOnTouchListener(new MyTouchListener());
        btnAccept.setOnDragListener(new MyDragListener());
        btnReject.setOnDragListener(new MyDragListener());
        btnSms.setOnDragListener(new MyDragListener());

        if (getIntent().hasExtra(Call.KEY)) {
            call = (Call) getIntent().getSerializableExtra(Call.KEY);
        } else {
            call = new Call();
        }

        tvName.setText(call.name);
    }

    private class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {

                ClipData clipData = ClipData.newPlainText("", "");

                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(clipData, dragShadowBuilder, v, 0);

                v.setVisibility(View.GONE);

                circle.setVisibility(View.VISIBLE);
                callAnim.setVisibility(View.GONE);

                return true;
            }

            return false;
        }
    }

    private class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            if (action == DragEvent.ACTION_DRAG_ENTERED) {
                v.setActivated(true);
            } else if (action == DragEvent.ACTION_DRAG_EXITED) {
                v.setActivated(false);
            } else if (action == DragEvent.ACTION_DROP) {
                int id = v.getId();
                if (id == R.id.btnAccept) {
                    Intent intent = new Intent(AndroidM_IncomingCall.this, AndroidM_InCall.class);
                    intent.putExtra(Call.KEY, call);
                    startActivity(intent);
                    finish();

                } else if (id == R.id.btnReject) {
                    Toast.makeText(getApplicationContext(), "Reject", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.btnSms) {
                    Toast.makeText(getApplicationContext(), "SMS", Toast.LENGTH_SHORT).show();
                }
            } else if (action == DragEvent.ACTION_DRAG_ENDED) {
                v.setActivated(false);
                final View dragView = (View) event.getLocalState();

                dragView.post(new Runnable() {
                    @Override
                    public void run() {
                        dragView.setVisibility(View.VISIBLE);
                        circle.setVisibility(View.GONE);
                        callAnim.setVisibility(View.VISIBLE);
                    }
                });
            }
            return true;
        }
    }
}
