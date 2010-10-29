package org.phxandroid.examples.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import android.view.View;

public class HttpPutActivity extends AbstractHttpActivity {

	@Override
	protected void doTest(View v) {
		URI uri = getDestinationURI();
		if (uri == null) {
			return;
		}

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPut httpput = new HttpPut(uri);
			setUserAgent(httpput);

			HttpResponse response = client.execute(httpput);
			StatusLine statusline = response.getStatusLine();

			if (statusline.getStatusCode() != HttpStatus.SC_OK) {
				printf("Failed HTTP PUT: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
				return;
			}

			printf("Content Length: %,d bytes%n", response.getEntity().getContentLength());
		} catch (IOException e) {
			printf(e, "Unable to HTTP PUT: %s%n", uri);
		}
	}

	@Override
	protected String getDefaultDestination() {
		return "http://cant.find.a.good.example.site/";
	}
}
