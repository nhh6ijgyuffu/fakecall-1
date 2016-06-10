package com.alex.fakecall.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<TabItem> list;
    private boolean showTitle;

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
    }

    public void addTab(String title, Fragment frag) {
        list.add(new TabItem(frag, title));
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position).frag;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (!showTitle) ? null : list.get(position).title;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static class TabItem {
        public Fragment frag;
        public String title;

        public TabItem(Fragment frag, String title) {
            this.frag = frag;
            this.title = title;
        }
    }
}
