package org.phxandroid.examples.state;

import org.phxandroid.examples.AbstractTestConsoleActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

public class NetworkStateActivity extends AbstractTestConsoleActivity {
	@Override
	protected String getDefaultDestination() {
		return "";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideDestination();

		printf("Welcome to Network State Example%n");
	}

	@Override
	protected void doTest(View v) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connMgr == null) {
			printf("couldn't get ConnectivityManager%n");
		} else {
			printAllNetworkInfos(connMgr);
			printActiveNetworkInfo(connMgr);
		}

		TelephonyManager telefony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		printTelefonyState(telefony);

		printf("%n");
		printf("## isNetworkAvailable = %s%n", isNetworkAvailable(connMgr));
		printf("## isOnWifi = %s%n", isOnWifi(connMgr));
		printf("## isOnMobileWithoutRoaming = %s%n", isOnMobileWithoutRoaming(connMgr, telefony));
	}

	private boolean isNetworkAvailable(ConnectivityManager connMgr) {
		if (connMgr == null) {
			return false;
		}
		NetworkInfo[] infos = connMgr.getAllNetworkInfo();
		if (infos == null) {
			return false;
		}

		for (NetworkInfo info : infos) {
			if (info.getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
		}
		return false;
	}

	private boolean isOnMobileWithoutRoaming(ConnectivityManager connMgr, TelephonyManager telefony) {
		if (connMgr == null) {
			return false;
		}

		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		if ((info.getType() != ConnectivityManager.TYPE_MOBILE) || (info.getState() != State.CONNECTED)) {
			return false;
		}
		// Now check for roaming
		if (telefony == null) {
			return false;
		}
		return !telefony.isNetworkRoaming();
	}

	private boolean isOnWifi(ConnectivityManager connMgr) {
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return ((info.getType() == ConnectivityManager.TYPE_WIFI) && (info.getState() == State.CONNECTED));
	}

	private void printTelefonyState(TelephonyManager telefony) {
		if (telefony == null) {
			printf("Couldn't get TelephonyManager%n");
			return;
		}

		printf("TelephonyManager:%n");
		printf("  .isNetworkRoaming: %s%n", Boolean.valueOf(telefony.isNetworkRoaming()));
	}

	private void printActiveNetworkInfo(ConnectivityManager connMgr) {
		NetworkInfo info = connMgr.getActiveNetworkInfo();
		if (info == null) {
			printf("network information is unavailable (No Active Network Info)%n");
			return;
		}

		printf("Active NetworkInfo:%n");
		print(info);
	}

	private void printAllNetworkInfos(ConnectivityManager connMgr) {
		NetworkInfo[] infos = connMgr.getAllNetworkInfo();
		if (infos == null) {
			printf("network information is unavailable%n");
			return;
		}

		printf("All NetworkInfos:%n");
		for (NetworkInfo info : infos) {
			print(info);
		}
	}

	private void print(NetworkInfo info) {
		if (info == null) {
			printf("NetworkInfo: <null>%n");
			return;
		}

		printf("NetworkInfo[%d]: %s%n", info.getType(), info.getTypeName());
		printf("   .subtype[%s]: %s%n", info.getSubtype(), info.getSubtypeName());
		printf("   .state: %s%n", getNetworkInfoState(info));
	}

	private String getNetworkInfoState(NetworkInfo info) {
		if (info == null) {
			return "UNKNOWN";
		}
		return info.getState().name();
	}
}
