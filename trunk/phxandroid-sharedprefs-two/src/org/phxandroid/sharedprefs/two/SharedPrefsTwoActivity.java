package org.phxandroid.sharedprefs.two;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class SharedPrefsTwoActivity extends Activity {
    private static final String PREFERENCE_ID = "PhxAndroid";
    public static final String  TAG           = "SharedPrefsTwo";
    public static final int     OPT_LOAD      = 1;
    public static final int     OPT_SAVE      = 2;
    public static final int     OPT_DUMP      = 3;
    private EditText            input;
    private TextView            output;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        input = (EditText) findViewById(R.id.edit);
        output = (TextView) findViewById(R.id.out);
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
                doLoad();
                break;
            case OPT_SAVE:
                doSave();
                break;
            case OPT_DUMP:
                doDump();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String userFriendly(String val) {
        if (TextUtils.isEmpty(val)) {
            return "<unset>";
        }
        return "\"" + val + "\"";
    }

    private void doLoad() {
        Log.i(TAG, "Load");

        SharedPreferences prefs = getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
        String actid = prefs.getString("actid", "");
        String actval = prefs.getString("actval", "");

        output.append("Loaded shared preferences...\n");
        output.append(String.format("actid = %s%n", userFriendly(actid)));
        output.append(String.format("actval = %s%n", userFriendly(actval)));
        output.append("Load complete\n");
    }

    private void doSave() {
        Log.i(TAG, "Save");

        SharedPreferences prefs = getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        String actid = this.getClass().getSimpleName();
        String actval = input.getText().toString();

        output.append("Saving shared preferences...\n");
        editor.putString("actid", actid);
        editor.putString("actval", actval);
        output.append(String.format("actid = %s%n", userFriendly(actid)));
        output.append(String.format("actval = %s%n", userFriendly(actval)));
        output.append("Save complete\n");

        editor.commit();
    }

    private void doDump() {
        Log.i(TAG, "Load");
        SharedPreferences prefs = getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);

        output.append("Dumping shared preferences...\n");
        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
            Object val = entry.getValue();
            if (val == null) {
                output.append(String.format("%s = <null>%n", entry.getKey()));
            } else {
                output.append(String.format("%s = %s (%s)%n", entry.getKey(), String.valueOf(val), val.getClass()
                        .getSimpleName()));
            }
        }
        output.append("Dump complete\n");
    }
}