package com.alex.fakecall.utils;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.alex.fakecall.FakeCall;
import com.alex.fakecall.models.Call;

public class PhoneUtils {

    public static void addCallLog(Activity activity, Call call) {
        ContentResolver resolver = activity.getContentResolver();

        ContentValues values = new ContentValues();

        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.TYPE, call.getType());
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, call.getDuration());
        values.put(CallLog.Calls.CACHED_NAME, call.getCallerName());
        values.put(CallLog.Calls.NUMBER, call.getCallerNumber());

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_CALL_LOG}, 1);
            return;
        }

        resolver.insert(CallLog.Calls.CONTENT_URI, values);
    }
}
