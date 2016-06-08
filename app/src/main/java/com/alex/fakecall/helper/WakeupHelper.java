package com.alex.fakecall.helper;


import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class WakeupHelper {
    private static WakeupHelper mInstance;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager.KeyguardLock mKeyLock;

    private WakeupHelper() {
    }

    public static synchronized WakeupHelper getInstance() {
        if (mInstance == null) {
            mInstance = new WakeupHelper();
        }
        return mInstance;
    }

    public void wakeUp(Context context) {
        if (mWakeLock == null) {
            PowerManager pwm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pwm.newWakeLock(805306394, "wakelock");
        }
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();

        if (mKeyLock == null) {
            KeyguardManager kgm = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            mKeyLock = kgm.newKeyguardLock("keyLock");
        }
        mKeyLock.disableKeyguard();
    }

    public void reset() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mWakeLock = null;
        if (mKeyLock != null) {
            mKeyLock.reenableKeyguard();
        }
        mKeyLock = null;
    }
}
