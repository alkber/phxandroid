package org.phxandroid.net.servers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.os.AsyncTask;

/**
 * TCP Server for Samuel Clemens Quotes.
 */
public class ClemensTCPServer {
    private static final String NAME = ClemensTCPServer.class.getSimpleName();
    private static final int    port = 1835;
    private List<String>        quotes;
    private Console             console;
    private ResponseTask        task;

    public ClemensTCPServer(Resources resources, Console console) {
        this.console = console;

        quotes = new ArrayList<String>();
        for (String quote : resources.getStringArray(R.array.clemens)) {
            quotes.add(quote);
        }
    }

    class ResponseTask extends AsyncTask<Void, String, Void> {
        private ServerSocket server;
        
        @Override
        protected void onPreExecute() {
            try {
                server = new ServerSocket(port);
                SocketAddress addr = server.getLocalSocketAddress();
                String localaddr = "0.0.0.0";
                if ((addr != null) && (addr instanceof InetSocketAddress)) {
                    InetSocketAddress iaddr = (InetSocketAddress) addr;
                    localaddr = iaddr.getAddress().getHostAddress();
                }

                progressf("Starting %s on %s port %d%n", NAME, localaddr, server.getLocalPort());
            } catch (IOException e) {
                progressf("Error initializing server %d%n", NAME);
                publishProgress("\n" + ExceptionUtils.asString(e) + "\n");
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Socket sock;
            SocketAddress remote;
            int size;

            while (!isCancelled()) {
                sock = null;
                try {
                    sock = server.accept();
                    remote = sock.getRemoteSocketAddress();
                    progressf("Accepted %s TCP Socket from %s%n", NAME, AddrUtil.asId(remote));
                    size = sendRandomQuote(sock);
                    progressf("(sent %d bytes)%n", size);
                } catch (IOException e) {
                    progressf("Error accepting new %d TCP socket%n", NAME);
                    publishProgress("\n" + ExceptionUtils.asString(e) + "\n");
                } finally {
                    close(sock);
                }
            }
            progressf("Shutting down server %s%n", NAME);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            console.printf("%s Shut down%n", NAME);
            if (server != null) {
                try {
                    server.close();
                } catch (IOException ignore) {
                    /* ignore */
                }
            }
        }

        private int sendRandomQuote(Socket sock) throws IOException {
            OutputStream stream = null;
            try {
                stream = sock.getOutputStream();
                byte quote[] = getRandomQuote();
                stream.write(quote);
                return quote.length;
            } finally {
                IOUtil.close(stream);
            }
        }

        private byte[] getRandomQuote() {
            Random random = new Random();
            int len = quotes.size();
            StringBuilder quote = new StringBuilder();
            quote.append('"');
            quote.append(quotes.get(random.nextInt(len)));
            quote.append("\"\n");
            quote.append(" - Mark Twain\n");
            return quote.toString().getBytes();
        }

        private void close(Socket sock) {
            if (sock == null) {
                return;
            }

            try {
                sock.close();
            } catch (IOException ignore) {
                /* ignore */
            }
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

    public void start() throws IOException {
        if (task == null) {
            task = new ResponseTask();
            this.task.execute();
        }
    }

    public void stop() throws IOException {
        if (this.task != null) {
            this.task.cancel(true);

        }
    }
}
