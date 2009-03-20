package org.phxandroid.contacts;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class ContactBrowser extends Activity {
    @SuppressWarnings("unused")
    private static final String TAG        = "ContactBrowser";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView grid = (GridView) findViewById(R.id.grid);
        
        grid.setAdapter(new ContactAdapter(this));
    }
}