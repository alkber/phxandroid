package org.phxandroid.examples;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class NetworkingExampleActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.examples);
    }
}