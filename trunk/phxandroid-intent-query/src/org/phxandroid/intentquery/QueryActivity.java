package org.phxandroid.intentquery;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QueryActivity extends Activity {
    private static final String TAG = "PackageManagerActivity";

    class DumpLauncherQuery implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);

            Log.i(TAG, "Dumping Launcher Query: found " + resolves.size() + " hits");

            for (ResolveInfo info : resolves) {
                dumpInfo(info);
            }
        }
    }

    class DumpGetContentImagesQuery implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent,
                    PackageManager.GET_INTENT_FILTERS);
            Log.i(TAG, "Dumping query: found " + resolves.size() + " hits");

            for (ResolveInfo info : resolves) {
                dumpInfo(info);
            }
        }
    }

    class DumpContactsIntents implements OnClickListener {
        @Override
        public void onClick(View v) {

            try {
                PackageInfo contactInfo = getPackageManager().getPackageInfo("com.android.contacts",
                         PackageManager.GET_INTENT_FILTERS);

                for (ActivityInfo activity : contactInfo.activities) {
                    Intent intent = new Intent();
                    intent.setClassName(contactInfo.packageName, activity.name);
                    List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);

                    for (ResolveInfo info : resolves) {
                        dumpInfo(info);
                    }
                }
            } catch (NameNotFoundException e) {
                Log.w(TAG, "Unable to get contacts package info", e);
            }
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnLauncher = (Button) findViewById(R.id.btnLauncher);
        Button btnGetContent = (Button) findViewById(R.id.btnGetContentImages);
        Button btnDumpContacts = (Button) findViewById(R.id.btnDumpContacts);

        btnLauncher.setOnClickListener(new DumpLauncherQuery());
        btnGetContent.setOnClickListener(new DumpGetContentImagesQuery());
        btnDumpContacts.setOnClickListener(new DumpContactsIntents());
    }

    private void dumpInfo(ResolveInfo info) {
        info.dump(new LogPrinter(Log.INFO, TAG), "DUMP/");
        Log.i(TAG, "info: " + info.activityInfo.packageName + " / " + info.activityInfo.targetActivity);
    }
}