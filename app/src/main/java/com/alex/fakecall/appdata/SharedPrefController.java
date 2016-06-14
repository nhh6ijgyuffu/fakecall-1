package com.alex.fakecall.appdata;


import android.content.SharedPreferences;

import com.alex.fakecall.FakeCallApp;
import com.alex.fakecall.utils.GsonUtils;

public class SharedPrefController {
    private static SharedPrefController mInstance;
    private SharedPreferences mPrefs;

    private static final String PREF_NAME = "fake_call";
    public static final String KEY_LAST_CALL = "last_call";


    public static synchronized SharedPrefController getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefController();
        }
        return mInstance;
    }

    private SharedPrefController() {
        mPrefs = FakeCallApp.getInstance().getSharedPreferences(PREF_NAME, 0);
    }

    public void saveObject(String key, Object object) {
        String objJson = GsonUtils.toJson(object);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, objJson);
        editor.apply();
    }

    public <T> T getObject(String key, Class<T> clazz) {
        String objJson = mPrefs.getString(key, "");
        return GsonUtils.fromJson(objJson, clazz);
    }
}
