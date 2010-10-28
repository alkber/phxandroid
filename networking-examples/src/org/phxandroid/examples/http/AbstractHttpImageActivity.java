package org.phxandroid.examples.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpRequestBase;
import org.phxandroid.examples.AbstractTestConsoleWithImageActivity;
import org.phxandroid.examples.utils.AndroidInfo;

import android.os.Bundle;
import android.text.TextUtils;

public abstract class AbstractHttpImageActivity extends AbstractTestConsoleWithImageActivity {
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
}
