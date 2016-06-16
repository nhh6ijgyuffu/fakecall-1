package com.alex.fakecall.data;


import android.os.Environment;
import android.util.Log;

import com.alex.fakecall.BuildConfig;

import java.io.File;

public class Variables {
    public static final String ACTION_CALL_RECEIVER;
    public static final String APP_FOLDER;
    public static final String VOICE_FOLDER;

    static {
        ACTION_CALL_RECEIVER = "com.alex.fakecall.ACTION_CALL_RECEIVER";

        APP_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID;
        File appDir = new File(APP_FOLDER);
        if (!appDir.exists() && appDir.mkdir()) {
            Log.i("GlobalVar", "FakeCallApp Dir created");
        }

        VOICE_FOLDER = APP_FOLDER + "/voice";
        File voiceDir = new File(VOICE_FOLDER);
        if (!voiceDir.exists() && voiceDir.mkdir()) {
            Log.i("GlobalVar", "Voice Dir created");
        }
    }
}
