package com.alex.fakecall.adapters;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.ChooseThemeActivity;
import com.alex.fakecall.models.Theme;

import butterknife.BindView;

public class ThemesAdapter extends BaseRecyclerViewAdapter<Theme, ThemesAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.theme_item);
    }

    protected class ViewHolder extends BaseRecyclerViewVH<Theme> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.selectedIndicator)
        ImageView ivSelected;

        @BindView(R.id.ivIncomingUI)
        ImageView ivIncomingUI;

        @BindView(R.id.ivInCallUI)
        ImageView ivInCallUI;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }


        @Override
        protected void onBind(final Theme item, final int pos) {
            tvName.setText(item.getName());
            ivInCallUI.setImageResource(item.getInCallRes());
            ivIncomingUI.setImageResource(item.getIncomingRes());

            if (item.equals(ChooseThemeActivity.selectedTheme)) {
                ivSelected.setVisibility(View.VISIBLE);
            } else {
                ivSelected.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.equals(ChooseThemeActivity.selectedTheme)) {
                        ChooseThemeActivity.selectedTheme = item;
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }


}
