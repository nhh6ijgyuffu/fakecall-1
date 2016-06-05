package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.PhoneUIsAdapter;
import com.alex.fakecall.call_ui.Android6XUI;
import com.alex.fakecall.models.PhoneUI;

import butterknife.BindView;

public class PhoneUISelectorActivity extends BaseActivity implements PhoneUIsAdapter.OnListCallback {
    @BindView(R.id.rvListUI)
    RecyclerView rvListUI;

    @Override
    public int getLayoutResource() {
        return R.layout.ui_call_selector;
    }

    @Override
    protected void onSetUp() {
        rvListUI.setLayoutManager(new LinearLayoutManager(this));
        rvListUI.setHasFixedSize(true);

        PhoneUIsAdapter adapter = new PhoneUIsAdapter(this);

        adapter.addItem(new PhoneUI(
                "Google Nexus",
                "Android M",
                R.drawable.android6x_preview_incoming,
                R.drawable.android6x_preview_incall,
                Android6XUI.class));

        rvListUI.setAdapter(adapter);
    }

    @Override
    protected void onCleanUp() {

    }

    @Override
    public void onPhoneUISelect(PhoneUI ui) {
        Intent intent = new Intent();
        intent.putExtra(PhoneUI.KEY, ui);
        setResult(RESULT_OK, intent);
        finish();
    }
}
