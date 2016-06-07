package com.alex.fakecall.fragments;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.NewCallActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ListCallFragment extends BaseFragment {
    @BindView(R.id.rvListCall)
    RecyclerView rvListCall;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_call;
    }

    @Override
    protected void setUp() {
        rvListCall.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListCall.setHasFixedSize(true);
    }

    @OnClick(R.id.fabNewCall)
    void onNewCall() {
        Intent intent = new Intent(getContext(), NewCallActivity.class);
        startActivity(intent);
    }
}
