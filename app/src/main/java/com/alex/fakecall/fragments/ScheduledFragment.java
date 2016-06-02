package com.alex.fakecall.fragments;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.SchedulesAdapter;
import com.alex.fakecall.data.Database;

import butterknife.BindView;

public class ScheduledFragment extends BaseFragment {
    @BindView(R.id.rvCallScheduled)
    RecyclerView rvCallScheduled;

    private SchedulesAdapter schedulesAdapter;
    private Database db;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_scheduled;
    }

    @Override
    protected void setUp() {
        rvCallScheduled.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCallScheduled.setHasFixedSize(true);

        schedulesAdapter = new SchedulesAdapter();
        rvCallScheduled.setAdapter(schedulesAdapter);

        db = new Database(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        schedulesAdapter.setList(db.getAllCalls());
    }
}
