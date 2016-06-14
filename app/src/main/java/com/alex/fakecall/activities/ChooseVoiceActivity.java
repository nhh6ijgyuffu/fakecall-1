package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.TabViewPagerAdapter;
import com.alex.fakecall.fragments.VoiceFragment;

import butterknife.BindView;

public class ChooseVoiceActivity extends BaseActivity {
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    public static String selectedUri;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_voice;
    }

    @Override
    protected void onSetUp() {
        setUpViewPager();
    }

    private void setUpViewPager() {
        final TabViewPagerAdapter mTabAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        mTabAdapter.setShowTitle(true);

        selectedUri = getIntent().getStringExtra("mUri");

        mTabAdapter.addTab(getString(R.string.lb_tab_recorded), VoiceFragment.newInstance(VoiceFragment.TYPE_RECORDED));
        mTabAdapter.addTab(getString(R.string.lb_tab_all), VoiceFragment.newInstance(VoiceFragment.TYPE_OTHER));

        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Fragment f = mTabAdapter.getItem(position);
                ((VoiceFragment) f).updateWhenSelected();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_action_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mnItem = menu.add("OK");
        mnItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mnItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent data = new Intent();
                data.putExtra("mUri", selectedUri);
                setResult(RESULT_OK, data);
                selectedUri = null;
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
