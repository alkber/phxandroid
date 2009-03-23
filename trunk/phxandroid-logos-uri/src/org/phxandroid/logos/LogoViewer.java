package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class LogoViewer extends Activity {
    public static final Uri URI = Uri.parse("logo://");
    private static final String TAG = "LogoViewer";
    private AssetAdapter        adapter;

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

            AssetRef ref;

            if (TextUtils.isDigitsOnly(path)) {
                // Fetch via position
                int position = Integer.parseInt(path);
                ref = adapter.getAssetRef(Integer.parseInt(path));
                if (ref == null) {
                    showErrorAndFinish("Logo not found", "Cannot find AssetRef at position: " + position);
                    return;
                }
            } else {
                // Fetch via name
                ref = adapter.getAssetRef(path);
                if (ref == null) {
                    showErrorAndFinish("Logo not found", "Cannot find AssetRef for name: " + path);
                    return;
                }
            }

            ImageView img = (ImageView) findViewById(R.id.logo);
            img.setImageBitmap(ref.getBitmap());
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
}
