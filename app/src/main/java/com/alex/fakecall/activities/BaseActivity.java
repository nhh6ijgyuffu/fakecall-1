package com.alex.fakecall.activities;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alex.fakecall.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutResource();

    protected abstract void onSetUp();

    protected abstract void onCleanUp();

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        onSetUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        onCleanUp();
        super.onDestroy();
    }

    protected void replaceFragment(@IdRes int containerId, Fragment frag, boolean backStack) {
        String tag = frag.getClass().getSimpleName();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(containerId, frag);
        if (backStack)
            trans.addToBackStack(tag);
        else
            trans.disallowAddToBackStack();
        trans.commit();
    }
}
