package com.alex.fakecall.models;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.alex.fakecall.utils.Utils;

import java.io.File;

public class VoiceFile {
    public static final String TAG = "VoiceFile";
    private Uri fileUri;
    private String name;
    private long duration;
    private int sizeKB;

    public VoiceFile(File file) {
        Uri uri = Uri.fromFile(file);
        fileUri = uri;
        name = file.getName();
        duration = Utils.getDurationOfAudioFile(uri);
        sizeKB = (int) (file.length() / 1000);
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSizeKB() {
        return sizeKB;
    }

    public String getFormattedDuration() {
        int seconds = (int) (duration / 1000) % 60;
        int minutes = (int) ((duration / (1000 * 60)) % 60);
        int hours = (int) ((duration / (1000 * 60 * 60)) % 24);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
