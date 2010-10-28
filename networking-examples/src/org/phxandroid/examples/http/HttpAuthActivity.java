package org.phxandroid.examples.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.phxandroid.examples.utils.IOUtil;

import android.view.View;

public class HttpAuthActivity extends AbstractHttpActivity {

    @Override
    protected void doTest(View v) {
        URI uri = getDestinationURI();
        if (uri == null) {
            return;
        }

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            setUserAgent(httpget);

            HttpResponse response = client.execute(httpget);
            StatusLine statusline = response.getStatusLine();

            if (statusline.getStatusCode() != HttpStatus.SC_OK) {
                printf("Failed HTTP GET: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
                return;
            }

            printf("Content Length: %,d bytes%n", response.getEntity().getContentLength());
            JSONArray json = readJSONArray(response);
            int updateCount = json.length();
            printf("JSON.keys.size = %,d%n", updateCount);
            int maxCount = Math.min(json.length(), 5);
            for(int i=0; i<maxCount; i++) {
                JSONObject update = json.getJSONObject(i);
                printf("Update %d: %s%n", i, update.getString("text"));
            }
        } catch (IOException e) {
            printf(e, "Unable to HTTP GET: %s%n", uri);
        } catch (JSONException e) {
            printf(e, "Unable to parse JSON: %s%n", e.getMessage());
        }
    }
    
    protected JSONArray readJSONArray(HttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new JSONException("No response content found.");
        }
    
        String content = readContentAsString(entity);
        return new JSONArray(content);
    }

    protected String readContentAsString(HttpEntity entity) throws IOException {
        InputStream in = null;
        InputStreamReader reader = null;
        try {
            in = entity.getContent();
            reader = new InputStreamReader(in);
            return IOUtil.readAsString(reader);
        } finally {
            IOUtil.close(reader);
            IOUtil.close(in);
        }
    }

    @Override
    protected String getDefaultDestination() {
        return "http://api.twitter.com/1/statuses/user_timeline.json?screen_name=IAM_SHAKESPEARE";
    }
}
