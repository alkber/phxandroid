package org.phxandroid.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.phxandroid.examples.utils.IOUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class AbstractTestConsoleActivity extends Activity {
    private TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        setContentView(R.layout.testconsole);
    
        Button btnTest = (Button) findViewById(R.id.btnTest);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        console = (TextView) findViewById(R.id.console);
    
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

    protected void printf(String format, Object... args) {
        console.append(String.format(format, args));
    }
    
    protected void printHtml(String format, Object... args) {
        console.append(Html.fromHtml(String.format(format, args)));
    }

    protected void doClearConsole(View v) {
        console.setText("");
    }

    protected abstract void doTest(View v);

    protected void printf(Throwable t, String format, Object ... args) {
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
    
    public NetApplication getNetApplication() {
        return (NetApplication) getApplication();
    }
}
