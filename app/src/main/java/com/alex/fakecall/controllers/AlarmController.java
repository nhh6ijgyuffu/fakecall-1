package com.alex.fakecall.controllers;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.FakeCallApp;
import com.alex.fakecall.data.Variables;
import com.alex.fakecall.models.Call;

public class AlarmController {
    private static AlarmController mInstance;
    private AlarmManager mAlarmManager;

    private AlarmController() {
    }

    public static synchronized AlarmController getInstance() {
        if (mInstance == null) {
            mInstance = new AlarmController();
        }
        return mInstance;
    }

    public void scheduleCall(int id, Call call) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) FakeCallApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        }

        Intent broadcast = new Intent(Variables.ACTION_CALL_RECEIVER);
        broadcast.putExtra(Call.TAG, call);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(FakeCallApp.getInstance(), id, broadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, call.getTime(), pendingIntent);
    }

    public void cancelCall(int id) {
        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) FakeCallApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(FakeCallApp.getInstance(), id, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
