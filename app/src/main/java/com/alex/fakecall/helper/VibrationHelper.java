package com.alex.fakecall.helper;


import android.content.Context;
import android.os.Vibrator;

import com.alex.fakecall.App;

public class VibrationHelper {
    private static VibrationHelper mInstance;
    private Vibrator mVibrator;

    public static synchronized VibrationHelper getInstance() {
        if (mInstance == null) {
            mInstance = new VibrationHelper();
        }
        return mInstance;
    }

    private VibrationHelper() {
    }

    public void vibrate(boolean repeat) {
        if (mVibrator == null) {
            mVibrator = (Vibrator) App.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (repeat) {
            mVibrator.vibrate(new long[]{1000, 1000, 1000, 1000}, 0);
        } else {
            mVibrator.vibrate(new long[]{100, 10, 100, 1000}, -1);
        }
    }

    public void cancelAll() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) App.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.cancel();
        mVibrator = null;
    }
}
