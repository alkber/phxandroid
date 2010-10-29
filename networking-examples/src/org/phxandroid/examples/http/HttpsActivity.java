package org.phxandroid.examples.http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.phxandroid.examples.html.HtmlPage;
import org.phxandroid.examples.ssl.PermissiveSSLSocketFactory;
import org.phxandroid.examples.utils.ErrorUtil;

import android.os.AsyncTask;
import android.view.View;

public class HttpsActivity extends AbstractHttpActivity {
	class GetResponse extends AsyncTask<URI, String, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(URI... uris) {
			URI uri = uris[0];

			try {
				HttpParams parameters = new BasicHttpParams();
				HttpProtocolParams.setVersion(parameters, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(parameters, HTTP.UTF_8);
				// some webservers have problems with "expect: continue" enabled
				HttpProtocolParams.setUseExpectContinue(parameters, false); 
				ConnManagerParams.setMaxTotalConnections(parameters, 2);
				HttpConnectionParams.setConnectionTimeout(parameters, 1000);
				HttpConnectionParams.setSoTimeout(parameters, 1000);

				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				schReg.register(new Scheme("https", PermissiveSSLSocketFactory.getSocketFactory(), 443));

				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(parameters, schReg);

				DefaultHttpClient client = new DefaultHttpClient(conMgr, parameters);

				progressf("Initiating HTTP GET to %s%n", uri);

				long startNS = System.nanoTime();
				HttpGet httpget = new HttpGet(uri);
				setUserAgent(httpget);

				HttpResponse response = client.execute(httpget);
				StatusLine statusline = response.getStatusLine();

				long endNS = System.nanoTime();
				progressf("Got response in %,d nanoseconds%n", (endNS - startNS));

				if (statusline.getStatusCode() != HttpStatus.SC_OK) {
					progressf("Failed HTTPS GET: %d %s%n", statusline.getStatusCode(), statusline.getReasonPhrase());
					return null;
				}

				progressf("Content Length: %,d bytes%n", response.getEntity().getContentLength());
				return response;
			} catch (IOException e) {
				progressf(e, "Unable to HTTPS GET: %s%n", uri);
			}
			return null;
		}

		private void progressf(String format, Object... args) {
			publishProgress(String.format(format, args));
		}

		private void progressf(Throwable t, String format, Object... args) {
			publishProgress(ErrorUtil.getStackTraceAsString(t));
			publishProgress(String.format(format, args));
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			if (response == null) {
				return;
			}
			printf("%nGot Response:%n");
			for (Header header : response.getAllHeaders()) {
				printf("%s: %s%n", header.getName(), header.getValue());
			}
			try {
				HtmlPage page = new HtmlPage(response.getEntity());
				printf("%nHtml Page:%n");
				printf("<title>%s</title>%n", page.getTitle());
			} catch (IOException e) {
				printf("Unable to parse html page: %s%n", e.getMessage());
			}
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

		new GetResponse().execute(uri);
	}

	@Override
	protected String getDefaultDestination() {
		return "https://mail.erdfelt.com/";
	}
}
