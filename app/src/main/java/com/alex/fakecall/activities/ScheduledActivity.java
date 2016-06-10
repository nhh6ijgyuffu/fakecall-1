package com.alex.fakecall.activities;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.TabViewPagerAdapter;
import com.alex.fakecall.fragments.ListScheduledCallFragment;
import com.alex.fakecall.fragments.NewSMSFragment;

import butterknife.BindView;

public class ScheduledActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_scheduled;
    }

    @Override
    protected void onSetUp() {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.setShowTitle(true);

        adapter.addTab(getString(R.string.lb_tab_call), new ListScheduledCallFragment());
        adapter.addTab(getString(R.string.lb_tab_sms), new NewSMSFragment());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onCleanUp() {

    }
}
