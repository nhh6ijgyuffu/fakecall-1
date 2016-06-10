package com.alex.fakecall.adapters;

import android.media.MediaMetadataRetriever;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.models.AudioObj;

import butterknife.BindView;


public class ListAudioAdapter extends BaseRecyclerViewAdapter<AudioObj, ListAudioAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.audio_item);
    }

    public class ViewHolder extends BaseRecyclerViewVH<AudioObj> {
        @BindView(R.id.tvPrimary)
        TextView tvName;

        @BindView(R.id.tvSecondary)
        TextView tvLength;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(AudioObj item, int pos) {

        }
    }
}
