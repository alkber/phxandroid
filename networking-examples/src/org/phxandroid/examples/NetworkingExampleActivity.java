package org.phxandroid.examples;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class NetworkingExampleActivity extends PreferenceActivity implements OnPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.examples);

        EditTextPreference serverhost = (EditTextPreference) findPreference(Prefs.KEY_SERVER_HOSTNAME);
        serverhost.setOnPreferenceChangeListener(this);
        NetApplication netapp = getNetApplication();
        serverhost.setSummary(netapp.getServerHostname());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();

        if (Prefs.KEY_SERVER_HOSTNAME.equals(key)) {
            EditTextPreference serverhost = (EditTextPreference) preference;
            String hostname = newValue.toString();
            NetApplication netapp = getNetApplication();
            netapp.setServerHostname(hostname);
            serverhost.setSummary(hostname);
        }

        return false;
    }

    public NetApplication getNetApplication() {
        return (NetApplication) getApplication();
    }
}