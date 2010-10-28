package org.phxandroid.examples.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.phxandroid.examples.utils.ErrorUtil;
import org.phxandroid.examples.utils.IOUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

public class HttpPostTaskActivity extends AbstractHttpImageActivity {

    class PostChartTask extends AsyncTask<URI, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(URI... uris) {
            URI uri = uris[0]; // only interested in first one.
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(uri);
                setUserAgent(post);

                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("cht", "qr"));
                parameters.add(new BasicNameValuePair("chs", "300x300"));
                parameters.add(new BasicNameValuePair("chl", "\"Be yourself; everyone else is already taken.\" ~Oscar Wilde"));

                UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters);
                post.setEntity(form);

                progressf("Submitting POST...%n");
                long startNS = System.nanoTime();
                HttpResponse response = client.execute(post);
                StatusLine statusline = response.getStatusLine();
                long endNS = System.nanoTime();
                progressf("Got response in %,d nanoseconds%n", (endNS - startNS));

                if (statusline.getStatusCode() != HttpStatus.SC_OK) {
                    progressf("Failed HTTP POST: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
                    return null;
                }

                HttpEntity entity = response.getEntity();
                progressf("Content Length: %,d bytes%n", entity.getContentLength());
                InputStream stream = null;
                try {
                    stream = entity.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    long postbitmapNS = System.nanoTime();
                    progressf("Decode Bitmap took %,d nanoseconds%n", (postbitmapNS - endNS));
                    return bitmap;
                } finally {
                    IOUtil.close(stream);
                }
            } catch (IOException e) {
                progressf(e, "Unable to HTTP POST: %s%n", uri);
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
        protected void onPostExecute(Bitmap result) {
            setImage(result);
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

        printf("Submitting Task%n");
        (new PostChartTask()).execute(uri);

    }

    @Override
    protected String getDefaultDestination() {
        return "http://chart.apis.google.com/chart";
    }
}
