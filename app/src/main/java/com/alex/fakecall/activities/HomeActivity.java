package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.fragments.NewCallFragment;
import com.alex.fakecall.fragments.NewSMSFragment;
import com.alex.fakecall.helper.DatabaseHelper;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.views.BadgeView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private BadgeView mnItemScheduled;

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        List<Fragment> listFrags;
        List<String> listTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            listFrags = new ArrayList<>();
            listTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return listFrags.get(position);
        }

        public void addTab(String title, Fragment fragment) {
            listFrags.add(fragment);
            listTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTitles.get(position);
        }

        @Override
        public int getCount() {
            return Math.min(listFrags.size(), listTitles.size());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void onSetUp() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        setUpViewPager();
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

    void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Call lastCall = DatabaseHelper.getInstance().getLastSavedCall();
        adapter.addTab("New Call", NewCallFragment.newInstance(lastCall, false));
        adapter.addTab("New SMS", new NewSMSFragment());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onCleanUp() {

    }
}
