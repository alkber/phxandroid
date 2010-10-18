package org.phxandroid.examples.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.phxandroid.examples.AbstractTestConsoleActivity;
import org.phxandroid.examples.utils.IOUtil;

import android.os.Bundle;
import android.view.View;

public class TcpActivity extends AbstractTestConsoleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        printf("Welcome to TCP Socket Example%n");
    }

    @Override
    protected void doTest(View v) {
        // String desthost = getNetApplication().getServerHostname();
        String desthost = "10.0.2.2";
        int port = 21835; // The Clemens TCP Server from networking-server-examples
        SocketAddress addr = null;
        Socket socket = null;
        InputStream stream = null;

        try {
            addr = new InetSocketAddress(desthost, port);
            socket = new Socket();
            socket.setSoTimeout(2000);
            socket.connect(addr);
            stream = socket.getInputStream();
            copyToConsole(stream);
        } catch (UnknownHostException e) {
            printf(e, "Unable to resolve host: %s%n", desthost);
        } catch (IOException e) {
            printf(e, "Unable to initiate Socket to host: %s%n", desthost);
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
