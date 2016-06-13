package com.alex.fakecall.controllers;


import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import com.alex.fakecall.FakeCallApp;

public class WakeUpController {
    private static WakeUpController mInstance;
    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager.KeyguardLock mKeyLock;

    private WakeUpController() {

    }

    public static synchronized WakeUpController getInstance() {
        if (mInstance == null) {
            mInstance = new WakeUpController();
        }
        return mInstance;
    }

    public void wakeUp() {
        if (mWakeLock == null) {
            PowerManager pwm = (PowerManager) FakeCallApp.getInstance().getSystemService(Context.POWER_SERVICE);
            mWakeLock = pwm.newWakeLock(805306394, "wakelock");
        }
        mWakeLock.setReferenceCounted(false);
        mWakeLock.acquire();

        if (mKeyLock == null) {
            KeyguardManager kgm = (KeyguardManager) FakeCallApp.getInstance()
                    .getSystemService(Context.KEYGUARD_SERVICE);
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
