package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.ThemesAdapter;
import com.alex.fakecall.models.Theme;
import com.alex.fakecall.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChooseThemeActivity extends BaseActivity {
    @BindView(R.id.rvListUI)
    RecyclerView rvThemes;

    public static int selectedId;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_choose_theme;
    }

    @Override
    protected void onSetUp() {
        selectedId = getIntent().getIntExtra("themeId", 0);

        toolbar.setNavigationIcon(R.drawable.ic_action_close);

        rvThemes.setLayoutManager(new LinearLayoutManager(this));
        rvThemes.setHasFixedSize(true);

        ThemesAdapter mAdapter = new ThemesAdapter();
        mAdapter.setList(Utils.getListThemes());
        rvThemes.setAdapter(mAdapter);
    }

    private void setResult() {
        Intent intent = new Intent();
        Theme selectedTheme = Utils.getThemeById(selectedId);
        intent.putExtra(Theme.TAG, selectedTheme);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mnItem = menu.add("OK");
        mnItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mnItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setResult();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
