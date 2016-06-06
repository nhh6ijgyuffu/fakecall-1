package com.alex.fakecall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.call_ui.Android6xActivity;


public class FakeCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent callIntent = new Intent(context, Android6xActivity.class);
        callIntent.putExtras(intent.getExtras());
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }
}
