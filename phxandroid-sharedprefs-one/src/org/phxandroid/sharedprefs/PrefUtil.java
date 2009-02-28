package org.phxandroid.sharedprefs;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.TextView;

public class PrefUtil {
    private static final String TEXT          = "phx.text";
    private static final String CONTEXT_ID    = "phx.context";
    private static final String PREFERENCE_ID = "PhxAndroid";

    private static String userFriendly(String val) {
        if (TextUtils.isEmpty(val)) {
            return "<unset>";
        }
        return "\"" + val + "\"";
    }

    public static void load(Context context, TextView output) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
        String phxText = prefs.getString(TEXT, "");
        String phxContext = prefs.getString(CONTEXT_ID, "");

        output.append("Loaded shared preferences...\n");
        output.append(String.format("%s = %s%n", CONTEXT_ID, userFriendly(phxText)));
        output.append(String.format("%s = %s%n", TEXT, userFriendly(phxContext)));
        output.append("Load complete\n");
    }

    public static void save(Context context, TextView output, String textval) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();

        String phxContext = context.getClass().getSimpleName();

        output.append("Saving shared preferences...\n");
        editor.putString(CONTEXT_ID, phxContext);
        editor.putString(TEXT, textval);
        output.append(String.format("%s = %s%n", CONTEXT_ID, userFriendly(phxContext)));
        output.append(String.format("%s = %s%n", TEXT, userFriendly(textval)));
        output.append("Save complete\n");

        editor.commit();
    }

    public static void dump(Context context, TextView output) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);

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

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE);
    }
}
