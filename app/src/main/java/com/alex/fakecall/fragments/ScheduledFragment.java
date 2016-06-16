package com.alex.fakecall.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.EditCallActivity;
import com.alex.fakecall.adapters.BaseRecyclerViewAdapter;
import com.alex.fakecall.adapters.ScheduledCallsAdapter;
import com.alex.fakecall.controllers.AlarmController;
import com.alex.fakecall.data.AppDB;
import com.alex.fakecall.utils.DialogUtils;
import com.alex.fakecall.models.Call;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import butterknife.BindView;

public class ScheduledFragment extends BaseFragment {
    @BindView(R.id.rvListCall)
    RecyclerView rvListCall;

    private ScheduledCallsAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_scheduled_calls;
    }

    @Override
    protected void onSetUp() {
        setHasOptionsMenu(true);
        rvListCall.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListCall.setHasFixedSize(true);

        mAdapter = new ScheduledCallsAdapter();

        rvListCall.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        rvListCall.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(getContext(), EditCallActivity.class);
                intent.putExtra(Call.TAG, (Call) item);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, final Object item, final int position) {
                DialogUtils.showPopupMenu(getContext(), v, R.array.list_item_opt, Gravity.RIGHT, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem mn) {
                        switch (mn.getItemId()) {
                            case 0:
                                cancelCall((Call) item);
                                mAdapter.removeItem(position);
                                break;
                        }
                        return false;
                    }
                });
                return false;
            }
        });
    }

    private void cancelCall(Call call) {
        AlarmController.getInstance().cancelCall((int) call.getId());
        AppDB.getInstance().deleteCall(call.getId());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem mnDelete = menu.add(getString(R.string.mn_opt_delete));
        mnDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mnDelete.setIcon(R.drawable.ic_action_delete);
        mnDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogUtils.showConfirmationDialog(getContext(), null, getString(R.string.lb_delete_all), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            for (Call c : mAdapter.getList()) {
                                cancelCall(c);
                            }
                            mAdapter.clearList();
                        }
                    }
                });

                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setList(AppDB.getInstance().getAllCalls());
    }
}
