package org.phxandroid.examples.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.phxandroid.examples.AbstractTestConsoleActivity;
import org.phxandroid.examples.utils.AndroidInfo;
import org.phxandroid.examples.utils.IOUtil;

import android.os.Bundle;
import android.text.TextUtils;

public abstract class AbstractHttpActivity extends AbstractTestConsoleActivity {
    private String userAgent;

    public URI getDestinationURI() {
        String deststr = getDestinationText();
        if (TextUtils.isEmpty(deststr)) {
            displayError("Not a valid destination.  Need an http or https URI");
            return null;
        }

        try {
            return new URI(deststr);
        } catch (URISyntaxException e) {
            displayError("Not a valid destination URI: %s", deststr);
            return null;
        }
    }

    protected void setUserAgent(HttpRequestBase httpbase) {
        httpbase.setHeader("User-Agent", this.userAgent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAgent = AndroidInfo.getUniqueDeviceID(this);
    }

    protected JSONObject readJSONObject(HttpResponse response) throws IOException, JSONException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new JSONException("No response content found.");
        }
    
        String content = readContentAsString(entity);
        return new JSONObject(content);
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
}
