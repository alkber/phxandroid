package org.phxandroid.intentquery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("IntentReceiver", "Got intent " + intent.getAction() + " / data: " + intent.getDataString());
    }
}
