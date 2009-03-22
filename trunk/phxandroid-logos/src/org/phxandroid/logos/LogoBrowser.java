package org.phxandroid.logos;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Gallery;

public class LogoBrowser extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Gallery gallery = (Gallery) findViewById(R.id.logo_gallery);
        try {
            AssetAdapter adapter = new AssetAdapter(this);
            gallery.setAdapter(new AssetAdapter(this));
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
}