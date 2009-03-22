package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);
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

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("name", ref.getName());
        intent.setComponent(new ComponentName(this, LogoViewer.class));
        startActivity(intent);
    }
}