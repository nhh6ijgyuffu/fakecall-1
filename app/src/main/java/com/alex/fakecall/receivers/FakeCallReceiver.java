package com.alex.fakecall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.controllers.DatabaseController;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.utils.Utils;


public class FakeCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent == null) return;
        Call call = intent.getParcelableExtra(Call.TAG);

        if (call.getTheme() != null) {
            Intent callIntent = Utils.getCallingIntent(ctx, call.getTheme().getId());
            if (callIntent != null) {
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.putExtra(Call.TAG, call);
                ctx.startActivity(callIntent);
            }
        }
        call.setAlarmed(true);
        DatabaseController.getInstance().updateCall(call);
    }
}
