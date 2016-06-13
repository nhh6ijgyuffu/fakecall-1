package com.alex.fakecall.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.Utils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import butterknife.BindView;

public class ScheduledCallsAdapter extends BaseRecyclerViewAdapter<Call, ScheduledCallsAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<ScheduledCallsAdapter.SectionVH> {

    @Override
    public ScheduledCallsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.scheduled_call_item);
    }

    @Override
    public long getHeaderId(int position) {
        boolean isAlarmed = getItem(position).isAlarmed();
        return isAlarmed ? 1 : 0;
    }

    @Override
    public SectionVH onCreateHeaderViewHolder(ViewGroup parent) {
        return new SectionVH(parent, R.layout.list_section_item);
    }

    @Override
    public void onBindHeaderViewHolder(SectionVH holder, int position) {
        holder.onBind(getItem(position), position);
    }

    public class ViewHolder extends BaseRecyclerViewVH<Call> {
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvNumber)
        TextView tvNumber;

        @BindView(R.id.tvTime)
        TextView tvTime;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(final Call item, final int pos) {
            tvName.setText(item.getName());
            tvNumber.setText(item.getNumber());
            tvTime.setText(Utils.millisToString(item.getTime(), " dd MMM yyyy, hh:mm:ss a"));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onItemClick(v, item, pos);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return longClickListener != null
                            && longClickListener.onItemLongClick(v, item, pos);
                }
            });
        }
    }

    public class SectionVH extends BaseRecyclerViewVH<Call> {
        @BindView(R.id.tvSection)
        TextView tvSection;

        public SectionVH(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(Call item, int pos) {
            if (item.isAlarmed()) {
                tvSection.setText(R.string.lb_alarmed);
            } else {
                tvSection.setText(R.string.lb_pending);
            }
        }
    }

}
