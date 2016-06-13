package com.alex.fakecall;


import android.app.Application;


public class FakeCallApp extends Application {
    private static FakeCallApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized FakeCallApp getInstance() {
        return mInstance;
    }

}
