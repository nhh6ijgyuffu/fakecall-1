package com.alex.fakecall.activities;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.phone_ui.AndroidL_IncomingCall;
import com.alex.fakecall.activities.phone_ui.GS5_IncomingCall;
import com.alex.fakecall.adapters.ListPhoneUIAdapter;
import com.alex.fakecall.models.PhoneUI;

import butterknife.BindView;

public class PhoneUISelectorActivity extends BaseActivity {
    @BindView(R.id.rvListUI)
    RecyclerView rvListUI;

    @Override
    public int getLayoutResource() {
        return R.layout.ui_call_selector;
    }

    @Override
    protected void onStart() {
        super.onStart();

        rvListUI.setLayoutManager(new LinearLayoutManager(this));
        rvListUI.setHasFixedSize(true);

        ListPhoneUIAdapter adapter = new ListPhoneUIAdapter();
        adapter.addItem(new PhoneUI("Galaxy S5", "Android 5.0", GS5_IncomingCall.class));
        adapter.addItem(new PhoneUI("Lollipop", "Android 5.0", AndroidL_IncomingCall.class));

        rvListUI.setAdapter(adapter);
    }
}
