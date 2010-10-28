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

import android.view.View;

public class HttpPostActivity extends AbstractHttpImageActivity {

    @Override
    protected void doTest(View v) {
        URI uri = getDestinationURI();
        if (uri == null) {
            return;
        }

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(uri);
            setUserAgent(post);

            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("cht", "lc"));
            parameters.add(new BasicNameValuePair("chtt", "This is | my chart"));
            parameters.add(new BasicNameValuePair("chs", "300x200"));
            parameters.add(new BasicNameValuePair("chxt", "x"));
            parameters.add(new BasicNameValuePair("chd", "t:40,20,50,20,100"));

            UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameters);
            post.setEntity(form);

            HttpResponse response = client.execute(post);
            StatusLine statusline = response.getStatusLine();

            if (statusline.getStatusCode() != HttpStatus.SC_OK) {
                printf("Failed HTTP POST: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
                return;
            }

            HttpEntity entity = response.getEntity();
            printf("Content Length: %,d bytes%n", entity.getContentLength());
            InputStream stream = entity.getContent();
            setImage(stream);
        } catch (IOException e) {
            printf(e, "Unable to HTTP POST: %s%n", uri);
        }
    }

    @Override
    protected String getDefaultDestination() {
        return "http://chart.apis.google.com/chart";
    }
}
