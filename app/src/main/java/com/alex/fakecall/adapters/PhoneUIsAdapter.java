package com.alex.fakecall.adapters;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.models.Theme;

import butterknife.BindView;

public class PhoneUIsAdapter extends BaseRecyclerViewAdapter<Theme, PhoneUIsAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.phone_ui_item);
    }

    protected class ViewHolder extends BaseRecyclerViewVH<Theme> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.btnPreview)
        ImageView btnPreview;

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

            btnPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), item.getIncomingClass());
                    intent.putExtra("isPreview", true);
                    v.getContext().startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onItemClick(v, item, pos);
                }
            });
        }

    }
}
