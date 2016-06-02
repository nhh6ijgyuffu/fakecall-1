package com.alex.fakecall.activities;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.alex.fakecall.R;
import com.alex.fakecall.fragments.EmptyFrag;
import com.alex.fakecall.fragments.FakeCallFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    public int getLayoutResource() {
        return R.layout.main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpViewPager();
    }

    void setUpViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addTab("Fake Call", new FakeCallFragment());
        adapter.addTab("Fake SMS", new EmptyFrag());
        adapter.addTab("History", new EmptyFrag());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

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
}
