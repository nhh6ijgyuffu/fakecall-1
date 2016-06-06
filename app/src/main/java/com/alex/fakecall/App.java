package com.alex.fakecall;


import android.app.Application;

public class App extends Application {
    private static App mInstance;

    public static class GlobalVars {
        public static boolean isInFakeCall;
        public static final String ACTION_RECEIVER = "com.alex.fakecall.ACTION_RECEIVER";

        static {
            isInFakeCall = false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }
}
