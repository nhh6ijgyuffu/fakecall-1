package com.alex.fakecall.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.ListAudioAdapter;

import butterknife.BindView;

public class RecordedFragment extends BaseFragment {
    @BindView(R.id.rvListRecorded)
    RecyclerView rvListRecorded;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_list_recorded;
    }

    @Override
    protected void onSetUp() {
        rvListRecorded.setHasFixedSize(true);
        rvListRecorded.setLayoutManager(new LinearLayoutManager(getContext()));
        ListAudioAdapter mAdapter = new ListAudioAdapter();
        rvListRecorded.setAdapter(mAdapter);
    }
}
