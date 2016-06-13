package com.alex.fakecall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> listFrag;
    private List<String> listTitle;
    private boolean showTitle;

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
        listFrag = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    public void addTab(String title, Fragment frag) {
        listFrag.add(frag);
        listTitle.add(title);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return listFrag.get(position);
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (!showTitle) ? null : listTitle.get(position);
    }

    @Override
    public int getCount() {
        return Math.min(listFrag.size(), listTitle.size());
    }
}
