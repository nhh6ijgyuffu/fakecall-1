package com.alex.fakecall.fragments;


import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.call_ui.GS5_IncomingCall;
import com.alex.fakecall.views.LinearLayoutEx;

import butterknife.BindView;
import butterknife.OnClick;

public class FakeCallFragment extends BaseFragment {

    @BindView(R.id.lyMoreSettings)
    LinearLayoutEx lyMoreSettings;

    @BindView(R.id.tvShowMore)
    TextView tvShowMore;

    @BindView(R.id.ivExpandCollapse)
    ImageView ivExpandCollapse;

    @Override
    public int getLayoutResource() {
        return R.layout.fake_call;
    }

    @Override
    public void onStart() {
        super.onStart();
        lyMoreSettings.setOnToggleListener(new LinearLayoutEx.OnToggleListener() {
            @Override
            public void onChange(boolean isShowing) {
                if(isShowing){
                    tvShowMore.setText("Hide");
                    ivExpandCollapse.setImageResource(R.drawable.ic_collapse);
                }else{
                    tvShowMore.setText("More settings...");
                    ivExpandCollapse.setImageResource(R.drawable.ic_expand);
                }
            }
        });
    }

    @OnClick(R.id.btnMoreSettings)
    void onMoreSettings(){
        lyMoreSettings.toggle();
    }

    @OnClick(R.id.btnSave)
    void saveCall(){

    }
}
