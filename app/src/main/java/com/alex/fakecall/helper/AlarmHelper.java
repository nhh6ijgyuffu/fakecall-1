package com.alex.fakecall.helper;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.models.Call;
import com.alex.fakecall.receivers.FakeCallReceiver;

public class AlarmHelper {
    private static AlarmHelper mInstance;
    private AlarmManager mAlarmManager;
    private Context mContext;

    private AlarmHelper(Context ctx) {
        mContext = ctx;
        mAlarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

    public static synchronized AlarmHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new AlarmHelper(ctx);
        }
        return mInstance;
    }

    public void placeCall(Call call) {
        Intent broadcast = new Intent(mContext, FakeCallReceiver.class);
        broadcast.putExtra(Call.KEY, call);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, call.getId(), broadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, call.getTime(), pendingIntent);
    }

    public void cancelCall(int id) {
        Intent broadcast = new Intent(mContext, FakeCallReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, id, broadcast,
                PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        mAlarmManager.cancel(pendingIntent);
    }
}
