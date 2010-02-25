package org.phxandroid.logos;

import java.io.IOException;

import org.phxandroid.logos.AssetAdapter.AssetRef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.ImageView;

public class LogoViewer extends Activity {

    private AssetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            String name = getNameFromIntent();
            if (name == null) {
                showErrorAndFinish("Name not found", "The intent does not include the name of the logo to view.");
                return;
            }

            if (adapter == null) {
                adapter = AssetAdapter.getInstance();
                if (!adapter.isInitialized()) {
                    adapter.init(this);
                }
            }

            AssetRef ref = adapter.getAssetRef(name);
            if (ref == null) {
                showErrorAndFinish("Logo not found", "Cannot find AssetRef for name: " + name);
                return;
            }

            ImageView img = (ImageView) findViewById(R.id.logo);
            img.setImageBitmap(ref.getBitmap());
        } catch (IllegalStateException e) {
            showErrorAndFinish("Intent Missing or Invalid",
                    "This activity cannot be called directly, an intent with data has to be provided." + "\n"
                            + e.getMessage());
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
            public void onClick(DialogInterface dialog, int which) {
                doFinish();
            }
        });
        dlg.show();
    }

    private String getNameFromIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            throw new IllegalStateException("Intent is null");
        }

        Bundle bundle = intent.getExtras();
        ;
        if (bundle == null) {
            throw new IllegalStateException("Intent Extras is null");
        }

        return bundle.getString("name");
    }

    private void doFinish() {
        finish();
    }

}
