package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class LogoBrowser extends Activity {
    private AssetAdapter logos;

    class NormalItemClick implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetRef ref = logos.getAssetRef(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("name", ref.getName());
            intent.setClassName(LogoBrowser.this, LogoViewer.class.getName());
            startActivity(intent);
        }
    }

    class ShortcutItemClick implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetRef ref = logos.getAssetRef(position);

            // First we create the intent that will be used to Launch the viewer.
            Intent viewerIntent = new Intent(Intent.ACTION_VIEW);
            viewerIntent.setClassName(LogoBrowser.this, LogoViewer.class.getName());
            viewerIntent.putExtra("name", ref.getName());

            // Then, set up the container intent that is used by the Launcher to setup the icon, name, and intent to
            // launch.
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, viewerIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ref.getName() + " Logo");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, ref.getIcon());

            // Now, return the result to the launcher
            setResult(RESULT_OK, intent);
            doFinish();
        }
    }

    class SelectImageItemClick implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetRef ref = logos.getAssetRef(position);

            Intent imageIntent = new Intent();

            Intent getcontentIntent = getIntent();
            Bundle extras = getcontentIntent.getExtras();

            boolean returnData = extras.getBoolean("return-data", false);
            if (returnData) {
                int outputX = extras.getInt("outputX");
                int outputY = extras.getInt("outputY");

                Bitmap imageBitmap = Bitmap.createScaledBitmap(ref.getBitmap(), outputX, outputY, true);
                imageIntent.putExtra("data", imageBitmap);
            }

            setResult(RESULT_OK, imageIntent);
            doFinish();
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        Intent intent = getIntent();
        String action = intent.getAction();

        GridView grid = (GridView) findViewById(R.id.logos_grid);

        // Test the selection mode intents.
        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
            grid.setOnItemClickListener(new ShortcutItemClick());
        } else if (Intent.ACTION_GET_CONTENT.equals(action)) {
            grid.setOnItemClickListener(new SelectImageItemClick());
        } else {
            grid.setOnItemClickListener(new NormalItemClick());
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
        } catch (IOException e) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Asset Load Error");
            dlg.setMessage("Unable to load assets.\n" + e.getClass().getSimpleName() + "\n" + e.getMessage());
            dlg.setNeutralButton("Close", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    /* do nothing */
                }
            });
            dlg.show();
        }
    }

    public void doFinish() {
        finish();
    }
}