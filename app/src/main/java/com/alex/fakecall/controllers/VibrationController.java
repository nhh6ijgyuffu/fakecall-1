package com.alex.fakecall.controllers;


import android.content.Context;
import android.os.Vibrator;

import com.alex.fakecall.FakeCallApp;

public class VibrationController {
    private static VibrationController mInstance;
    private Vibrator mVibrator;

    public static synchronized VibrationController getInstance() {
        if (mInstance == null) {
            mInstance = new VibrationController();
        }
        return mInstance;
    }

    private VibrationController() {
    }

    public void vibrate(boolean repeat) {
        if (mVibrator == null) {
            mVibrator = (Vibrator) FakeCallApp.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        }
        if (repeat) {
            mVibrator.vibrate(new long[]{1000, 1000, 1000, 1000}, 0);
        } else {
            mVibrator.vibrate(new long[]{100, 10, 100, 1000}, -1);
        }
    }

    public void cancelAll() {
        if (mVibrator == null) {
            mVibrator = (Vibrator) FakeCallApp.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        }
        mVibrator.cancel();
        mVibrator = null;
    }
}
