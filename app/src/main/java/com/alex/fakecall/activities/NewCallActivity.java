package com.alex.fakecall.activities;


import com.alex.fakecall.R;
import com.alex.fakecall.fragments.NewCallFragment;
import com.alex.fakecall.models.Call;

public class NewCallActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_new_call;
    }

    @Override
    protected void onSetUp() {
        Call call = getIntent().getParcelableExtra(Call.TAG);
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        replaceFragment(R.id.contentContainer, NewCallFragment.newInstance(call, isEdit), false);
    }

}
