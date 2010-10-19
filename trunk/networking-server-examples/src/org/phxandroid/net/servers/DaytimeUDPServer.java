package org.phxandroid.net.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Date;

import android.os.AsyncTask;

public class DaytimeUDPServer {
    private static final String NAME = DaytimeUDPServer.class.getSimpleName();
    private int                 port = 1813;
    private Console             console;
    private ResponseTask        task;

    class ResponseTask extends AsyncTask<Void, String, Void> {
        private DatagramSocket server;

        @Override
        protected void onPreExecute() {
            try {
                server = new DatagramSocket(port);
                SocketAddress addr = server.getLocalSocketAddress();
                String localaddr = "0.0.0.0";
                if ((addr != null) && (addr instanceof InetSocketAddress)) {
                    InetSocketAddress iaddr = (InetSocketAddress) addr;
                    localaddr = iaddr.getAddress().getHostAddress();
                }

                progressf("Starting %s on %s port %d%n", NAME, localaddr, server.getLocalPort());
            } catch (SocketException e) {
                progressf("Unable to setup DatagramSocket for %s on port %d%n", NAME, port);
                publishProgress("\n" + ExceptionUtils.asString(e) + "\n");
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            byte dummy[] = new byte[0];
            DatagramPacket trigger = new DatagramPacket(dummy, dummy.length);
            SocketAddress remote;
            while (!isCancelled()) {
                try {
                    server.receive(trigger);
                    remote = trigger.getSocketAddress();
                    progressf("Got %s trigger packet from %s%n", NAME, AddrUtil.asId(remote));
                    server.send(getDaytimePacket(remote));
                    progressf("Sent UDP daytime to %s%n", NAME, AddrUtil.asId(remote));
                } catch (IOException e) {
                    progressf("%s: %s%n", e.getClass().getName(), e.getMessage());
                }
            }
            progressf("Shutting down server %s%n", NAME);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            console.printf("%s Shut down%n", NAME);
            if (server != null) {
                server.close();
            }
        }

        private DatagramPacket getDaytimePacket(SocketAddress socketAddress) throws SocketException {
            String now = (new Date()).toString();
            byte buf[] = now.getBytes();
            return new DatagramPacket(buf, buf.length, socketAddress);
        }

        private void progressf(String format, Object... args) {
            publishProgress(String.format(format, args));
        }

        @Override
        protected void onProgressUpdate(String... values) {
            for (String str : values) {
                console.printf(str);
            }
        }
    }

    public DaytimeUDPServer(Console console) {
        this.console = console;
    }

    public void start() throws IOException {
        if (task == null) {
            task = new ResponseTask();
            task.execute();
        }
    }

    public void stop() throws IOException {
        if (task != null) {
            task.cancel(true);
        }
    }
}
