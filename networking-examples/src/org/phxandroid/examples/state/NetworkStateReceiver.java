package org.phxandroid.examples.state;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {
	private static final String TAG = "NetworkStateReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(TAG, "Received Intent: " + action);
		
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "Receiver onConnectivity");

            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info == null) {
                Log.i(TAG, "No Network Info Available");
                // pause all downloads and retries.
                // cancel any pending async tasks.
                // stop service.
                // Release wakelocks (important!)
                return;
            }

            if (info.isConnected()) {
                Log.i(TAG, "Broadcast: Network Up");
                // Restart any downloads.
                // Send any pending transactions.
                // Start any async tasks.
                // (optional) Request partial wakelock to allow networking to continue.
                return;
            } else {
                Log.i(TAG, "Broadcast: Network Down");
                // pause all downloads and retries.
                // cancel any pending async tasks.
                // stop service.
                // Release wakelocks (important!)
            }
            return;
        }

	}

}
