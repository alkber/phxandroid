package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class LogoBrowser extends Activity implements OnItemClickListener {
    private AssetAdapter logos;
    private boolean      modeShortcut = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        Intent intent = getIntent();
        String action = intent.getAction();

        // If the intent is a request to create a shortcut, we'll trigger the shortcut mode
        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
            modeShortcut = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GridView grid = (GridView) findViewById(R.id.logos_grid);
        try {
            if (logos == null) {
                logos = AssetAdapter.getInstance();
                if (!logos.isInitialized()) {
                    logos.init(this);
                }
            }

            grid.setAdapter(logos);
            grid.setOnItemClickListener(this);
        } catch (IOException e) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Asset Load Error");
            dlg.setMessage("Unable to load assets.\n" + e.getClass().getSimpleName() + "\n" + e.getMessage());
            dlg.setNeutralButton("Close", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /* do nothing */
                }
            });
            dlg.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AssetRef ref = logos.getAssetRef(position);

        if (modeShortcut) {
            // Shortcut selection mode
            createShortcut(ref);
            finish();
        } else {
            // Normal click during normal operation.
            launchViewer(ref.getName());
        }
    }

    private void createShortcut(AssetRef ref) {
        // First we create the intent that will be used to Launch the viewer.
        Intent viewerIntent = new Intent(Intent.ACTION_VIEW);
        viewerIntent.setClassName(this, LogoViewer.class.getName());
        viewerIntent.putExtra("name", ref.getName());

        // Then, set up the container intent that is used by the Launcher to setup the icon, name, and intent to launch.
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, viewerIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ref.getName() + " Logo");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, ref.getIcon());

        // Now, return the result to the launcher
        setResult(RESULT_OK, intent);
    }

    private void launchViewer(String name) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("name", name);
        intent.setClassName(this, LogoViewer.class.getName());
        startActivity(intent);
    }
}