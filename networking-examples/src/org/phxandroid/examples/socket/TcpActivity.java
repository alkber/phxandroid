package org.phxandroid.examples.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.phxandroid.examples.AbstractTestConsoleActivity;
import org.phxandroid.examples.utils.IOUtil;

import android.os.Bundle;
import android.view.View;

public class TcpActivity extends AbstractTestConsoleActivity {
    @Override
    protected String getDefaultDestination() {
        return "10.0.2.2:21835";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        printf("Welcome to TCP Socket Example%n");
    }

    @Override
    protected void doTest(View v) {
        InetSocketAddress addr = getDestinationSocketAddress();
        if (addr == null) {
            return;
        }
        Socket socket = null;
        InputStream stream = null;

        try {
            socket = new Socket();
            socket.setSoTimeout(2000);
            socket.connect(addr);
            stream = socket.getInputStream();
            copyToConsole(stream);
        } catch (UnknownHostException e) {
            printf(e, "Unable to resolve host: %s%n", addr);
        } catch (IOException e) {
            printf(e, "Unable to initiate Socket to host: %s%n", addr);
        } finally {
            IOUtil.close(stream);
            close(socket);
        }
    }

    public static void close(Socket socket) {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (IOException ignore) {
            /* ignore */
        }
    }
}
