package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.phone_ui.AndroidM_IncomingCall;
import com.alex.fakecall.adapters.PhoneUIsAdapter;
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
    protected void setUp() {
        rvListUI.setLayoutManager(new LinearLayoutManager(this));
        rvListUI.setHasFixedSize(true);

        PhoneUIsAdapter adapter = new PhoneUIsAdapter();
        adapter.addItem(new PhoneUI(
                "Android Marshmallow",
                "Android M",
                R.drawable.preview_android6_incoming_ui,
                R.drawable.preview_android6_incall_ui,
                AndroidM_IncomingCall.class));

        adapter.setOnListCallback(new PhoneUIsAdapter.OnListCallback() {
            @Override
            public void onSelect(PhoneUI ui) {
                Intent intent = new Intent();
                intent.putExtra(PhoneUI.KEY, ui);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        rvListUI.setAdapter(adapter);
    }
}
