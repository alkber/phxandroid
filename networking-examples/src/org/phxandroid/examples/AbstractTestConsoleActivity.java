package org.phxandroid.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;

import org.phxandroid.examples.utils.IOUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public abstract class AbstractTestConsoleActivity extends Activity {
    protected TextView console;
    private EditText destination;
    
    protected int getLayoutId() {
        return R.layout.testconsole;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        Button btnTest = (Button) findViewById(R.id.btnTest);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        console = (TextView) findViewById(R.id.console);
        destination = (EditText) findViewById(R.id.destination);

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                saveDestionationText(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int atart, int count, int after) {
                /* ignore */
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /* ignore */
            }
        });

        SharedPreferences prefs = getSharedPreferences(this.getClass().getName(), 0);
        String destname = prefs.getString("destination", getDefaultDestination());
        destination.setText(destname);

        btnTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doTest(v);
            }
        });
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClearConsole(v);
            }
        });
        console.setText("");
    }

    protected void saveDestionationText(Editable s) {
        SharedPreferences prefs = getSharedPreferences(this.getClass().getName(), 0);
        Editor editor = prefs.edit();
        editor.putString("destination", s.toString());
        editor.commit();
    }

    protected void printf(String format, Object... args) {
        console.append(String.format(format, args));
    }

    protected void printHtml(String format, Object... args) {
        console.append(Html.fromHtml(String.format(format, args)));
    }

    protected void doClearConsole(View v) {
        console.setText("");
    }

    protected String getDestinationText() {
        return destination.getText().toString();
    }

    public InetSocketAddress getDestinationSocketAddress() {
        String deststr = destination.getText().toString();
        int idx = deststr.indexOf(':');
        if (idx <= 0) {
            displayError("Not a valid destination.  Format \"{hostname}:{port}\"");
            return null;
        }

        String host = deststr.substring(0, idx);
        String portstr = deststr.substring(idx + 1);

        try {
            int port = Integer.parseInt(portstr);
            return new InetSocketAddress(host, port);
        } catch (NumberFormatException e) {
            displayError("Not a valid destination port. \"%s\" is not a number", portstr);
            return null;
        }
    }

    public void displayError(String format, Object... args) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(String.format(format, args));
        builder.setCancelable(true);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected abstract void doTest(View v);

    protected abstract String getDefaultDestination();

    protected void printf(Throwable t, String format, Object... args) {
        printf(format, args);
        StringWriter writer = new StringWriter();
        writer.append("\n");
        t.printStackTrace(new PrintWriter(writer));
        writer.append("\n");
        console.append(writer.toString());
    }

    protected void copyToConsole(InputStream stream) throws IOException {
        long count = IOUtil.copy(stream, console);
        printHtml("<p><b>(Read %,d bytes from stream)</b></p>", count);
    }
}
