package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.TabViewPagerAdapter;
import com.alex.fakecall.fragments.NewCallFragment;
import com.alex.fakecall.fragments.NewSmsFragment;
import com.alex.fakecall.controllers.DatabaseController;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.views.BadgeView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private BadgeView mScheduleCounter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onSetUp() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());

        Call call = DatabaseController.getInstance().getLastSavedCall();

        adapter.addTab("New Call", NewCallFragment.newInstance(call, false));
        adapter.addTab("New SMS", new NewSmsFragment());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcons = new int[]{R.drawable.ic_tab_new_call, R.drawable.ic_tab_new_sms};
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setIcon(tabIcons[i]);
        }

        mScheduleCounter = new BadgeView(this);

        mScheduleCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ScheduledActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mnItem = menu.add(getString(R.string.lb_scheduled));
        mnItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mnItem.setActionView(mScheduleCounter);

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    private void updateCounter() {
        if (mScheduleCounter == null) return;
        YoYo.with(Techniques.Tada).duration(500).playOn(mScheduleCounter);
        int totalSize = DatabaseController.getInstance().getAllPendingCalls().size();
        mScheduleCounter.setCounter(totalSize);
    }
}
