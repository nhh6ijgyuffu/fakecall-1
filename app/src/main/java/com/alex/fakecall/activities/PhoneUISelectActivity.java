package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.PhoneUIsAdapter;
import com.alex.fakecall.call_ui.Android6xActivity;
import com.alex.fakecall.call_ui.GalaxyS6Activity;
import com.alex.fakecall.models.PhoneUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PhoneUISelectActivity extends BaseActivity implements PhoneUIsAdapter.OnListCallback {
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
        adapter.setList(getAvailablePhoneUI());
        rvListUI.setAdapter(adapter);
    }

    private List<PhoneUI> getAvailablePhoneUI() {
        List<PhoneUI> list = new ArrayList<>();

        PhoneUI android6 = new PhoneUI("Google Android 6.0", R.drawable.android6x_preview_incoming,
                R.drawable.android6x_preview_incall, Android6xActivity.class);

        PhoneUI gs6 = new PhoneUI("Samsung Galaxy S6 ", R.drawable.android6x_preview_incoming,
                R.drawable.android6x_preview_incall, GalaxyS6Activity.class);

        list.add(android6);
        list.add(gs6);
        return list;
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
