package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class LogoViewer extends Activity {
    public static final Uri     URI       = Uri.parse("logo://");
    private static final String TAG       = "LogoViewer";
    private static final int    OPT_EMAIL = 1;
    private AssetAdapter        adapter;
    private AssetRef            assetRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer);

        Uri uri = getIntent().getData();
        if (uri == null) {
            showErrorAndFinish("View Failed", "No Data found on Intent.");
            return;
        }

        Log.i(TAG, "data.uri=" + uri);
        Log.i(TAG, "data.uri.isAbsolute=" + uri.isAbsolute());
        Log.i(TAG, "data.uri.isHierarchical=" + uri.isHierarchical());
        Log.i(TAG, "data.uri.isOpaque=" + uri.isOpaque());
        Log.i(TAG, "data.uri.isRelative=" + uri.isRelative());
        Log.i(TAG, "data.uri.scheme=" + uri.getScheme());
        Log.i(TAG, "data.uri.host=" + uri.getHost());
        Log.i(TAG, "data.uri.port=" + uri.getPort());
        Log.i(TAG, "data.uri.path=" + uri.getPath());
        Log.i(TAG, "data.uri.query=" + uri.getQuery());
        Log.i(TAG, "data.uri.fragment=" + uri.getFragment());
        Log.i(TAG, "data.uri.schemeSpecificPart=" + uri.getSchemeSpecificPart());

        String path = uri.getSchemeSpecificPart();
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if ((path == null) || (path.trim().length() <= 0)) {
            showErrorAndFinish("View Failed", "No Path found on Intent.\n" + uri.toString());
            return;
        }

        try {
            if (adapter == null) {
                adapter = AssetAdapter.getInstance();
                if (!adapter.isInitialized()) {
                    adapter.init(this);
                }
            }

            assetRef = null;

            if (TextUtils.isDigitsOnly(path)) {
                // Fetch via position
                int position = Integer.parseInt(path);
                assetRef = adapter.getAssetRef(Integer.parseInt(path));
                if (assetRef == null) {
                    showErrorAndFinish("Logo not found", "Cannot find AssetRef at position: " + position);
                    return;
                }
            } else {
                // Fetch via name
                assetRef = adapter.getAssetRef(path);
                if (assetRef == null) {
                    showErrorAndFinish("Logo not found", "Cannot find AssetRef for name: " + path);
                    return;
                }
            }

            ImageView img = (ImageView) findViewById(R.id.logo);
            img.setImageBitmap(assetRef.getBitmap());
        } catch (IOException e) {
            showErrorAndFinish("IO Error", "Unable to load logo (or logos)." + "\n" + e.getClass().getSimpleName()
                    + "\n" + e.getMessage());
        }
    }

    private void showErrorAndFinish(String title, String message) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(title);
        dlg.setMessage(message);
        dlg.setNegativeButton("Close", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doFinish();
            }
        });
        dlg.show();
    }

    private void doFinish() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Create menus (in order)
        final MenuItem menuEmail = menu.add(0, OPT_EMAIL, 0, "EMail");

        // Establish icons for menus
        menuEmail.setIcon(android.R.drawable.ic_menu_send);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case OPT_EMAIL:
                doEmail();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doEmail() {
        String[] mailto = new String[] { "joakim.erdfelt@gmail.com" };
        String[] mailcc = new String[] { "joakim@erdfelt.com" };
        String subject = "[phxandroid-logos] Email";

        StringBuffer body = new StringBuffer();

        body.append("Auto generated email body from app\n");
        try {
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            body.append(info.packageName).append(" v").append(info.versionName);
            body.append(" (").append(info.versionCode).append(")\n");
        } catch (NameNotFoundException e) {
            body.append(getPackageName()).append(" (v)\n");
        }

        body.append("\nSelected Logo: " + assetRef.getName());

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, mailto);
        email.putExtra(Intent.EXTRA_CC, mailcc);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body.toString());
        email.setType("text/plain");

        startActivity(Intent.createChooser(email, "PhxAndroidSendEmail"));
    }
}
