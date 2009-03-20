package org.phxandroid.contacts;

import org.phxandroid.contacts.ContactAdapter.ContactInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ContactBrowser extends Activity implements OnItemClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = "ContactBrowser";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new ContactAdapter(this));
        grid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        ContactAdapter.ContactInfo info = (ContactInfo) v.getTag();
        Toast.makeText(this, "Clicked on " + info.name, Toast.LENGTH_LONG).show();
    }
}