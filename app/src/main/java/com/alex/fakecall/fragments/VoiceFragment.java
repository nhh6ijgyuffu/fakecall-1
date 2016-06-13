package com.alex.fakecall.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alex.fakecall.R;
import com.alex.fakecall.adapters.VoiceFilesAdapter;
import com.alex.fakecall.appdata.GlobalVars;
import com.alex.fakecall.controllers.AudioController;
import com.alex.fakecall.controllers.AudioController.PlayerTag;
import com.alex.fakecall.dialogs.RecordAudioDialog;
import com.alex.fakecall.models.VoiceFile;
import com.alex.fakecall.utils.ExtensionFilter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VoiceFragment extends BaseFragment {
    @BindView(R.id.rvListRecorded)
    RecyclerView rvListRecorded;

    @BindView(R.id.fabRecord)
    FloatingActionButton fabRecord;

    private VoiceFilesAdapter mAdapter;

    private int mType;
    public static final int TYPE_RECORDED = 0;
    public static final int TYPE_OTHER = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_voice;
    }

    public static VoiceFragment newInstance(int type) {
        VoiceFragment frag = new VoiceFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        frag.setArguments(args);
        return frag;
    }

    @Override
    protected void onSetUp() {
        mType = getArguments().getInt("type");
        rvListRecorded.setHasFixedSize(true);
        rvListRecorded.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VoiceFilesAdapter();
        rvListRecorded.setAdapter(mAdapter);
        new GetAudioFilesTask(this, mType).execute();
    }

    @OnClick(R.id.fabRecord)
    void onStartRecording() {
        RecordAudioDialog dialog = new RecordAudioDialog(getContext(),
                new RecordAudioDialog.OnRecordCompletedCallback() {
                    @Override
                    public void onCompleted(VoiceFile file) {
                        mAdapter.addItem(file);
                    }
                });
        dialog.show();
    }

    public void updateWhenSelected() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mType == TYPE_RECORDED) {
            fabRecord.setVisibility(View.VISIBLE);
        } else {
            fabRecord.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AudioController.getInstance().stopPlaying(PlayerTag.VOICE);
    }

    private static class GetAudioFilesTask extends AsyncTask<Void, Void, List<VoiceFile>> {
        private WeakReference<VoiceFragment> mWeakRef;
        private int mType;

        public GetAudioFilesTask(VoiceFragment frag, int type) {
            mWeakRef = new WeakReference<>(frag);
            mType = type;
        }


        @Override
        protected List<VoiceFile> doInBackground(Void... params) {
            if (mType == TYPE_RECORDED) {
                return fromRecorded();
            }
            return fromOther();
        }

        private List<VoiceFile> fromRecorded() {
            File voiceDir = new File(GlobalVars.VOICE_FOLDER);
            File[] files = voiceDir.listFiles(new ExtensionFilter(new String[]{"amr", "wav", "mp4"}));

            List<VoiceFile> list = new ArrayList<>();

            for (File file : files) {
                list.add(new VoiceFile(file));
            }
            return list;
        }

        private List<VoiceFile> fromOther() {
            VoiceFragment frag = mWeakRef.get();
            List<VoiceFile> list = new ArrayList<>();
            if (frag != null) {
                Uri queryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] projections = new String[]{MediaStore.Audio.Media.DATA};
                Cursor cursor = frag.getContext().getContentResolver()
                        .query(queryUri, projections, null, null, null);

                if (cursor == null) return list;
                if (cursor.moveToFirst()) {
                    do {
                        String path = cursor.getString(0);
                        if (!path.contains(GlobalVars.VOICE_FOLDER)) {
                            File file = new File(path);
                            list.add(new VoiceFile(file));
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<VoiceFile> audioFiles) {
            VoiceFragment frag = mWeakRef.get();
            if (frag != null) {
                frag.mAdapter.setList(audioFiles);
            }
        }
    }
}
