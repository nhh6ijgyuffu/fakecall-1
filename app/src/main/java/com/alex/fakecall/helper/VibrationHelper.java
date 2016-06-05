package com.alex.fakecall.helper;


import android.content.Context;
import android.os.Vibrator;

public class VibrationHelper {
    private static VibrationHelper mInstance;
    private Vibrator mVibrator;
    private Context mContext;

    public static synchronized VibrationHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new VibrationHelper(ctx);
        }
        return mInstance;
    }

    public VibrationHelper(Context ctx) {
        mContext = ctx;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(boolean repeat) {
        if (repeat) {
            mVibrator.vibrate(new long[]{1000, 1000, 1000, 1000}, 0);
        } else {
            mVibrator.vibrate(new long[]{100, 10, 100, 1000}, -1);
        }
    }

    public void cancelAll(){
        mVibrator.cancel();
    }
}
