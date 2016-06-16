package com.alex.fakecall.activities;


import com.alex.fakecall.R;
import com.alex.fakecall.fragments.CallFragment;
import com.alex.fakecall.models.Call;

public class EditCallActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_call;
    }

    @Override
    protected void onSetUp() {
        Call call = getIntent().getParcelableExtra(Call.TAG);
        replaceFragment(R.id.contentContainer, CallFragment.newInstance(call, true), false);
    }

}
