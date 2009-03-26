package org.phxandroid.intentquery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class QueryActivity extends Activity {
    private static final String TAG = "PackageManagerActivity";

    class DumpLauncherQuery implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    }

    class DumpQuery implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnLauncher = (Button) findViewById(R.id.btnLauncher);
        Button btnQuery = (Button) findViewById(R.id.btnQuery);

        btnLauncher.setOnClickListener(new DumpLauncherQuery());
        btnQuery.setOnClickListener(new DumpQuery());
    }
}