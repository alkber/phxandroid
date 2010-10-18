package org.phxandroid.net.servers;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.text.Html;
import android.widget.TextView;

public class Console {
    private TextView textview;

    public Console(TextView textview) {
        this.textview = textview;
    }

    public void printf(String format, Object... args) {
        textview.append(String.format(format, args));
    }

    public void printHtml(String format, Object... args) {
        textview.append(Html.fromHtml(String.format(format, args)));
    }

    public void printf(Throwable t, String format, Object... args) {
        printf(format, args);
        StringWriter writer = new StringWriter();
        writer.append("\n");
        t.printStackTrace(new PrintWriter(writer));
        writer.append("\n");
        textview.append(writer.toString());
    }

    public void clear() {
        textview.setText("");
    }
}
