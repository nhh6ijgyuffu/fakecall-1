package com.alex.fakecall.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.models.PhoneUI;

import butterknife.BindView;

public class PhoneUIsAdapter extends BaseRecyclerViewAdapter<PhoneUI, PhoneUIsAdapter.ViewHolder> {
    private OnListCallback callback;

    public PhoneUIsAdapter(Context context) {
        super(context);
        try {
            callback = (OnListCallback) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.phone_ui_item);
    }

    class ViewHolder extends BaseRecyclerViewVH<PhoneUI> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvOs)
        TextView tvOs;

        @BindView(R.id.btnPreview)
        ImageView btnPreview;

        @BindView(R.id.ivIncomingUI)
        ImageView ivIncomingUI;

        @BindView(R.id.ivIncallUI)
        ImageView ivIncallUI;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(final PhoneUI item, int pos) {
            tvName.setText(item.getName());
            tvOs.setText(item.getOs());
            ivIncallUI.setImageResource(item.getInCallRes());
            ivIncomingUI.setImageResource(item.getIncomingRes());

            btnPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, item.getIncoming_act());
                    intent.putExtra("isPreview", true);
                    mContext.startActivity(intent);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null)
                        callback.onPhoneUISelect(item);
                }
            });
        }
    }

    public interface OnListCallback {
        void onPhoneUISelect(PhoneUI ui);
    }
}
