package com.alex.fakecall.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.App;
import com.alex.fakecall.models.Call;

public class AlarmHelper {
    private static AlarmHelper mInstance;
    private AlarmManager mAlarmManager;

    private AlarmHelper() {

    }

    public static synchronized AlarmHelper getInstance() {
        if (mInstance == null) {
            mInstance = new AlarmHelper();
        }
        return mInstance;
    }

    public void placeCall(Context ctx, Call call) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        }

        Intent broadcast = new Intent(App.GlobalVars.ACTION_RECEIVER);
        broadcast.putExtra(Call.KEY, call);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, call.getId(), broadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, call.getTime(), pendingIntent);
    }

    public void cancelCall(Context ctx, int id) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, id, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
