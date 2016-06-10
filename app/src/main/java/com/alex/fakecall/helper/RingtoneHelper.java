package com.alex.fakecall.helper;


import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import com.alex.fakecall.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RingtoneHelper {
    private static RingtoneHelper mInstance;
    private MediaPlayer mPlayer;

    public static RingtoneHelper getInstance() {
        if (mInstance == null) {
            mInstance = new RingtoneHelper();
        }
        return mInstance;
    }

    private RingtoneHelper() {
    }

    public void playRingtone(String uriStr, boolean loop){
        playRingtone(Uri.parse(uriStr), loop);
    }

    public void playRingtone(Uri audioUri, boolean loop) {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(App.getInstance(), audioUri);
            mPlayer.prepare();
            mPlayer.setLooping(loop);
            mPlayer.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRingtone() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
    }

    public List<Ringtone> getAllRingtone() {
        RingtoneManager manager = new RingtoneManager(App.getInstance());
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();

        List<Ringtone> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            String id = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            Ringtone r = new Ringtone(Integer.valueOf(id), title, uri + "/" + id);
            list.add(r);
        }
        cursor.close();
        return list;
    }

    public static class Ringtone {
        public int id;
        public String name;
        public String uriStr;

        public Ringtone(int id, String name, String uriStr) {
            this.id = id;
            this.name = name;
            this.uriStr = uriStr;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
