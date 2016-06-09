package com.alex.fakecall.activities;


import com.alex.fakecall.R;
import com.alex.fakecall.fragments.NewCallFragment;
import com.alex.fakecall.models.Call;

public class EditCallActivity extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit_call;
    }

    @Override
    protected void onSetUp() {
        Call call = (Call) getIntent().getSerializableExtra(Call.KEY);
        boolean isEdit = getIntent().getBooleanExtra("isEdit", false);
        replaceFragment(R.id.contentContainer, NewCallFragment.newInstance(call, isEdit), false);
    }

    @Override
    protected void onCleanUp() {

    }
}
