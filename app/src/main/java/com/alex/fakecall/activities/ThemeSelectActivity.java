package com.alex.fakecall.activities;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.BaseRecyclerViewAdapter;
import com.alex.fakecall.adapters.PhoneUIsAdapter;
import com.alex.fakecall.themes.Android6xActivity;
import com.alex.fakecall.themes.GalaxyS6Activity;
import com.alex.fakecall.models.Theme;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ThemeSelectActivity extends BaseActivity {
    @BindView(R.id.rvListUI)
    RecyclerView rvThemes;

    @Override
    public int getLayoutResource() {
        return R.layout.activity_choose_theme;
    }

    @Override
    protected void onSetUp() {
        rvThemes.setLayoutManager(new LinearLayoutManager(this));
        rvThemes.setHasFixedSize(true);

        PhoneUIsAdapter adapter = new PhoneUIsAdapter();
        adapter.setList(getAvailableTheme());
        rvThemes.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Object item, int position) {
                Intent intent = new Intent();
                intent.putExtra(Theme.KEY, (Theme) item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<Theme> getAvailableTheme() {
        List<Theme> list = new ArrayList<>();

        Theme android6 = new Theme("Google Android 6.0", R.drawable.android6x_preview_incoming,
                R.drawable.android6x_preview_incall, Android6xActivity.class);

        Theme gs6 = new Theme("Samsung Galaxy S6 ", R.drawable.android6x_preview_incoming,
                R.drawable.android6x_preview_incall, GalaxyS6Activity.class);

        list.add(android6);
        list.add(gs6);
        return list;
    }

    @Override
    protected void onCleanUp() {

    }

}
