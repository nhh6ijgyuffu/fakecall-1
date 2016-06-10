package com.alex.fakecall.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.App;
import com.alex.fakecall.GlobalVariables;
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

    public void scheduleCall(int id, Call call) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);
        }

        Intent broadcast = new Intent(GlobalVariables.ACTION_CALL_RECEIVER);
        broadcast.putExtra(Call.KEY, call);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(App.getInstance(), id, broadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, call.getTime(), pendingIntent);
    }

    public void cancelCall(int id) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) App.getInstance().getSystemService(Context.ALARM_SERVICE);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(App.getInstance(), id, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
