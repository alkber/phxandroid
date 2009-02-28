package org.phxandroid.sharedprefs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class SharedPrefsTwoActivity extends Activity implements OnSharedPreferenceChangeListener {
    private static final String TAG      = "SharedPrefsTwo";
    public static final int     OPT_LOAD = 1;
    public static final int     OPT_SAVE = 2;
    public static final int     OPT_DUMP = 3;
    private EditText            input;
    private TextView            output;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_two);

        input = (EditText) findViewById(R.id.edit);
        output = (TextView) findViewById(R.id.out);

        PrefUtil.getPrefs(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        PrefUtil.getPrefs(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Create menus (in order)
        final MenuItem menuLoad = menu.add(0, OPT_LOAD, 0, "Load");
        final MenuItem menuSave = menu.add(0, OPT_SAVE, 1, "Save");
        final MenuItem menuDump = menu.add(0, OPT_DUMP, 3, "Dump");

        // Establish icons for menus
        menuLoad.setIcon(R.drawable.load);
        menuSave.setIcon(R.drawable.save);
        menuDump.setIcon(R.drawable.dump);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case OPT_LOAD:
                PrefUtil.load(this, output);
                break;
            case OPT_SAVE:
                PrefUtil.save(this, output, input.getText().toString());
                break;
            case OPT_DUMP:
                PrefUtil.dump(this, output);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        Log.i(TAG, "onSharedPreferenceChanged(prefs, '" + key + "')");
    }
}