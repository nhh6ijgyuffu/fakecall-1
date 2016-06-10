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
import com.alex.fakecall.fragments.NewSMSFragment;
import com.alex.fakecall.helper.DatabaseHelper;
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

    private BadgeView mnItemScheduled;

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

        Call call = DatabaseHelper.getInstance().getLastSavedCall();

        adapter.addTab("New Call", NewCallFragment.newInstance(call, false));
        adapter.addTab("New SMS", new NewSMSFragment());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int[] tabIcons = new int[]{R.drawable.ic_tab_new_call, R.drawable.ic_tab_new_sms};
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null)
                tab.setIcon(tabIcons[i]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem mnItem = menu.findItem(R.id.action_open_schedule);
        mnItemScheduled = (BadgeView) mnItem.getActionView();
        mnItemScheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ScheduledActivity.class));
            }
        });
        updateCounter();
        return true;
    }


    private void updateCounter() {
        if (mnItemScheduled == null) return;
        YoYo.with(Techniques.Tada).duration(500).playOn(mnItemScheduled);
        int totalSize = DatabaseHelper.getInstance().getAllPendingCalls().size();
        mnItemScheduled.setCounter(totalSize);
    }

    @Override
    protected void onCleanUp() {

    }
}
