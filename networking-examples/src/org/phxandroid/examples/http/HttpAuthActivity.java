package org.phxandroid.examples.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.phxandroid.examples.dialogs.CredentialsActivity;
import org.phxandroid.examples.html.HtmlPage;
import org.phxandroid.examples.utils.ErrorUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

public class HttpAuthActivity extends AbstractHttpActivity {
    private static final int CREDENTIALS_REQUEST = 2;
    
    class GetLinks extends AsyncTask<URI, String, HtmlPage> {
        @Override
        protected HtmlPage doInBackground(URI... uris) {
            URI uri = uris[0];
            HttpClient client = new DefaultHttpClient();
            HttpContext context = new BasicHttpContext();
            
            try {
                long startNS = System.nanoTime();
                HttpGet httpget = new HttpGet(uri);
                setUserAgent(httpget);

                HttpResponse response = client.execute(httpget, context);
                StatusLine statusline = response.getStatusLine();
                long endNS = System.nanoTime();
                progressf("Got response in %,d nanoseconds%n", (endNS - startNS));

                if (statusline.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                    progressf("%d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());

                    for(Header header: response.getAllHeaders()) {
                        progressf("%s: %s%n", header.getName(), header.getValue());
                    }
                    
                    AuthState authstate = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
                    progressf("authstate=%s%n", authstate);
                    if(authstate != null) {
                        AuthScope authscope = authstate.getAuthScope();
                        progressf("authscope=%s%n", authscope);
                        requestCredentials(authscope);
                    }
                    return null;
                }

                if (statusline.getStatusCode() != HttpStatus.SC_OK) {
                    progressf("Failed HTTP GET: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
                    return null;
                }

                progressf("Content Length: %,d bytes%n", response.getEntity().getContentLength());
                return new HtmlPage(response.getEntity());
            } catch (IOException e) {
                progressf(e, "Unable to HTTP GET: %s%n", uri);
                return null;
            }
        }

        private void progressf(String format, Object... args) {
            publishProgress(String.format(format, args));
        }

        private void progressf(Throwable t, String format, Object... args) {
            publishProgress(ErrorUtil.getStackTraceAsString(t));
            publishProgress(String.format(format, args));
        }

        @Override
        protected void onPostExecute(HtmlPage page) {
            if (page == null) {
                // TODO: show error.
            }
            printf("Got htmlpage: %s%n", page);
        }

        @Override
        protected void onProgressUpdate(String... lines) {
            for (String line : lines) {
                printf(line);
            }
        }
    }

    @Override
    protected void doTest(View v) {
        URI uri = getDestinationURI();
        if (uri == null) {
            return;
        }

        (new GetLinks()).execute(uri);
    }

    public void requestCredentials(AuthScope authscope) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("scheme", authscope.getScheme());
        intent.putExtra("realm", authscope.getRealm());
        intent.putExtra("host", authscope.getHost());
        intent.setClass(this, CredentialsActivity.class);
        startActivityForResult(intent, CREDENTIALS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case CREDENTIALS_REQUEST:
                switch(resultCode) {
                    case RESULT_OK:
                        printf("RESULT_OK%n");
                        break;
                    case RESULT_CANCELED:
                        printf("RESULT_CANCELED%n");
                        break;
                }
                break;
        }
    }

    @Override
    protected String getDefaultDestination() {
        return "http://joakim.erdfelt.com/gauntlet/";
    }
}
