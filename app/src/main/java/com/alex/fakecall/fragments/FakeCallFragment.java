package com.alex.fakecall.fragments;


import android.content.Intent;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.CallMoreSettingsActivity;
import com.alex.fakecall.activities.call_ui.GS5_IncomingCall;

import butterknife.OnClick;

public class FakeCallFragment extends BaseFragment {
    private static final int REQUEST_MORE_SETTINGS = 1;

    @Override
    public int getLayoutResource() {
        return R.layout.fake_call;
    }

    @OnClick(R.id.btnMoreSettings)
    void onMoreSettings(){
        Intent intent = new Intent(getActivity(), CallMoreSettingsActivity.class);
        startActivityForResult(intent, REQUEST_MORE_SETTINGS);
    }

    @OnClick(R.id.btnSave)
    void saveCall(){
        Intent intent = new Intent(getContext(), GS5_IncomingCall.class);
        startActivity(intent);
    }
}
