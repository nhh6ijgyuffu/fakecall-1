package com.alex.fakecall.adapters;


import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.Converter;

import butterknife.BindView;

public class SchedulesAdapter extends BaseRecyclerViewAdapter<Call, SchedulesAdapter.ViewHolder>{

    @Override
    public SchedulesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.scheduled_item);
    }

    class ViewHolder extends BaseRecyclerViewVH<Call> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvContent)
        TextView tvContent;

        @BindView(R.id.tvTime)
        TextView tvTime;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(Call item, int pos) {
            tvName.setText(item.name);
            tvContent.setText(item.number);

            String date = Converter.millis2String(item.alarm_time, "dd/MM/yyyy HH:mm:ss");
            tvTime.setText(date);
        }
    }
}
