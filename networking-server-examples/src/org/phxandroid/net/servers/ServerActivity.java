package org.phxandroid.net.servers;

import java.io.IOException;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ServerActivity extends Activity {
    private Console          console;
    private DaytimeUDPServer daytimeServer;
    private ClemensTCPServer clemensServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        TextView txtview = (TextView) findViewById(R.id.console);
        console = new Console(txtview);

        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart(v);
            }
        });
        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doStop(v);
            }
        });
        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                console.clear();
            }
        });

        console.clear();
        console.printf("Welcome to the Android Server examples%n");

        Resources resources = getResources();
        daytimeServer = new DaytimeUDPServer(console);
        clemensServer = new ClemensTCPServer(resources, console);
    }

    protected void doStop(View v) {
        console.printf("Issuing STOP%n");
        try {
            daytimeServer.stop();
        } catch (IOException e) {
            console.printf(e, "Unable to stop daytime server");
        }
        try {
            clemensServer.stop();
        } catch (IOException e) {
            console.printf(e, "Unable to stop clemens server");
        }
    }

    protected void doStart(View v) {
        console.printf("Issuing START%n");
        try {
            daytimeServer.start();
        } catch (IOException e) {
            console.printf(e, "Unable to start daytime server");
        }
        try {
            clemensServer.start();
        } catch (IOException e) {
            console.printf(e, "Unable to start clemens server");
        }
    }

}