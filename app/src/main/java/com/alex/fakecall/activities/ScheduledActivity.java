package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.BaseRecyclerViewAdapter;
import com.alex.fakecall.adapters.ScheduledCallsAdapter;
import com.alex.fakecall.helper.AlarmHelper;
import com.alex.fakecall.helper.DatabaseHelper;
import com.alex.fakecall.helper.DialogHelper;
import com.alex.fakecall.models.Call;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import butterknife.BindView;
import butterknife.OnClick;

public class ScheduledActivity extends BaseActivity {

    @BindView(R.id.rvListCall)
    RecyclerView rvListCall;

    private ScheduledCallsAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scheduled;
    }

    @Override
    protected void onSetUp() {
        rvListCall.setLayoutManager(new LinearLayoutManager(this));
        rvListCall.setHasFixedSize(true);

        mAdapter = new ScheduledCallsAdapter();

        rvListCall.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        rvListCall.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent(ScheduledActivity.this, EditCallActivity.class);
                intent.putExtra(Call.KEY, (Call) item);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View v, final Object item, final int position) {
                DialogHelper.showPopupMenu(ScheduledActivity.this, v, R.menu.list_item, Gravity.RIGHT, new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem mn) {
                        switch (mn.getItemId()) {
                            case R.id.mn_delete:
                                Call call = (Call) item;
                                AlarmHelper.getInstance().cancelCall(call.getId());
                                DatabaseHelper.getInstance().deleteCall(call.getId());
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

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.setList(DatabaseHelper.getInstance().getAllCalls());
    }

    @OnClick(R.id.fabNewCall)
    void onNewCall() {
        Intent intent = new Intent(this, EditCallActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCleanUp() {

    }
}
