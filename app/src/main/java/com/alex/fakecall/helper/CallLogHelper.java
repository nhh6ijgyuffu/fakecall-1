package com.alex.fakecall.helper;


import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;

import com.alex.fakecall.models.Call;

public class CallLogHelper {
    private static CallLogHelper mInstance;

    public static synchronized CallLogHelper getInstance() {
        if (mInstance == null) {
            mInstance = new CallLogHelper();
        }
        return mInstance;
    }

    private CallLogHelper() {
    }

    public void writeMissedCall(Context ctx, Call call) {
        writeCallLog(ctx, call, 0, CallLog.Calls.MISSED_TYPE);
    }

    public void writeIncomingCall(Context ctx, Call call, long duration) {
        writeCallLog(ctx, call, duration, CallLog.Calls.INCOMING_TYPE);
    }

    private void writeCallLog(Context ctx, Call call, long duration, int type) {
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.CACHED_NAME, call.getDisplayName());
        values.put(CallLog.Calls.NUMBER, call.getNumber());
        values.put(CallLog.Calls.DATE, System.currentTimeMillis());
        values.put(CallLog.Calls.DURATION, duration);

        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ctx.getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
    }
}
