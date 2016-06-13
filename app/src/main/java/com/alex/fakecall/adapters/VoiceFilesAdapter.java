package com.alex.fakecall.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alex.fakecall.R;
import com.alex.fakecall.activities.ChooseVoiceActivity;
import com.alex.fakecall.controllers.AudioController;
import com.alex.fakecall.models.VoiceFile;

import butterknife.BindView;


public class VoiceFilesAdapter extends BaseRecyclerViewAdapter<VoiceFile, VoiceFilesAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.audio_file_item);
    }

    public class ViewHolder extends BaseRecyclerViewVH<VoiceFile> {
        @BindView(R.id.tvPrimary)
        TextView tvName;

        @BindView(R.id.tvSecondary)
        TextView tvProperties;

        @BindView(R.id.radioButton)
        RadioButton radioButton;

        public ViewHolder(ViewGroup parent, int resId) {
            super(parent, resId);
        }

        @Override
        protected void onBind(final VoiceFile item, final int pos) {
            tvName.setText(item.getName());
            String properties = String.format("%s | %dKB", item.getFormattedDuration(), item.getSizeKB());
            tvProperties.setText(properties);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getFileUri().equals(ChooseVoiceActivity.selectedUri)) {
                        ChooseVoiceActivity.selectedUri = null;
                        AudioController.getInstance().stopPlaying(AudioController.PlayerTag.VOICE);
                    } else {
                        ChooseVoiceActivity.selectedUri = item.getFileUri();
                        AudioController.getInstance().startPlaying(AudioController.PlayerTag.VOICE,
                                item.getFileUri(), false);
                    }
                    notifyDataSetChanged();
                }
            });
            radioButton.setChecked(item.getFileUri().equals(ChooseVoiceActivity.selectedUri));
        }
    }
}
