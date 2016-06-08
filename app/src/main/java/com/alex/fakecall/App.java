package com.alex.fakecall;


import android.app.Application;


public class App extends Application {
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public static class GlobalVariables {
        public static final String ACTION_CALL_RECEIVER = "com.alex.fakecall.ACTION_CALL_RECEIVER";
    }
}