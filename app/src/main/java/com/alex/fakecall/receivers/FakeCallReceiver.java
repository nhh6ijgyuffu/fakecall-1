package com.alex.fakecall.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alex.fakecall.call_ui.Android6xActivity;
import com.alex.fakecall.models.Call;


public class FakeCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        if (intent == null) return;

        Call call = (Call) intent.getSerializableExtra(Call.KEY);
        Call.PhoneUI pui = call.getPhoneUI();
        Class<?> actClazz = pui == null ? Android6xActivity.class :pui.getIncomingClass();
        Intent callIntent = new Intent(ctx, actClazz);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.putExtra(Call.KEY, call);
        ctx.startActivity(callIntent);
    }
}
