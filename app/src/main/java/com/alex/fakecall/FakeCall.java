package com.alex.fakecall;


import android.app.Application;

public class FakeCall extends Application{
    private static FakeCall mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized FakeCall  getInstance(){
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }
}
