package com.alex.fakecall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.helper.DatabaseHelper;
import com.alex.fakecall.models.Call;
import com.alex.fakecall.themes.Android6xActivity;


public class FakeCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent == null) return;

        Call call = intent.getParcelableExtra(Call.KEY);

        Intent callIntent = new Intent(ctx, Android6xActivity.class);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.putExtra(Call.KEY, call);

        ctx.startActivity(callIntent);

        call.setAlarmed(true);

        DatabaseHelper.getInstance().updateCall(call);
    }
}
