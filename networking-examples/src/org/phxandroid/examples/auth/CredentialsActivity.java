package org.phxandroid.examples.auth;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.HttpContext;
import org.phxandroid.examples.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CredentialsActivity extends Activity {
	private static final String TAG = "CredentialsActivity";
	private static final String EXTRA_HOST = "host";
	private static final String EXTRA_REALM = "realm";
	private static final String EXTRA_SCHEME = "scheme";
	private static final String EXTRA_PORT = "port";
	private static final String EXTRA_USERNAME = "credentials.username";
	private static final String EXTRA_PASSWORD = "credentials.password";
	private EditText username;
	private EditText password;
	private AuthScope authscope;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credentials);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		Button btnOk = (Button) findViewById(R.id.btnOK);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doClickOk(v);
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doClickCancel(v);
			}
		});
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}

	protected void doClickCancel(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	protected void doClickOk(View v) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_USERNAME, username.getText().toString());
		intent.putExtra(EXTRA_PASSWORD, password.getText().toString());
		intent.putExtra(EXTRA_HOST, authscope.getHost());
		intent.putExtra(EXTRA_REALM, authscope.getRealm());
		intent.putExtra(EXTRA_SCHEME, authscope.getScheme());
		intent.putExtra(EXTRA_PORT, authscope.getPort());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void setTextView(int resource, String text) {
		TextView tv = (TextView) findViewById(resource);
		if (tv != null) {
			tv.setText(text);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		if (intent != null) {
			Bundle bundle = intent.getExtras();

			String host = bundle.getString(EXTRA_HOST);
			int port = bundle.getInt(EXTRA_PORT);
			String scheme = bundle.getString(EXTRA_SCHEME);
			String realm = bundle.getString(EXTRA_REALM);

			this.authscope = new AuthScope(host, port, realm, scheme);

			setTextView(R.id.host, authscope.getHost());
			setTextView(R.id.realm, authscope.getRealm());
			setTextView(R.id.scheme, authscope.getScheme());
		}
	}

	public static void collectCredentials(Activity activity, HttpContext context, int requestCode) {
		AuthState authstate = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
		if (authstate != null) {
			AuthScope authscope = authstate.getAuthScope();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.putExtra(EXTRA_SCHEME, authscope.getScheme());
			intent.putExtra(EXTRA_REALM, authscope.getRealm());
			intent.putExtra(EXTRA_HOST, authscope.getHost());
			intent.putExtra(EXTRA_PORT, authscope.getPort());
			intent.setClass(activity, CredentialsActivity.class);
			activity.startActivityForResult(intent, requestCode);
		}
	}

	public static void setCredentials(HttpContext context, Intent data) {
		AuthScope scope = getAuthScopeResult(data);
		Credentials creds = getCredentialsResult(data);

		CredentialsProvider credprov = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
		if (credprov == null) {
			credprov = new BasicCredentialsProvider();
			context.setAttribute(ClientContext.CREDS_PROVIDER, credprov);
		}
		credprov.setCredentials(scope, creds);
	}

	public static AuthScope getAuthScopeResult(Intent data) {
		String host = data.getStringExtra(EXTRA_HOST);
		int port = data.getIntExtra(EXTRA_PORT, 80);
		String scheme = data.getStringExtra(EXTRA_SCHEME);
		String realm = data.getStringExtra(EXTRA_REALM);

		AuthScope scope = new AuthScope(host, port, realm, scheme);

		Log.d(TAG, "Provided authscope = " + scope);

		return scope;
	}

	public static Credentials getCredentialsResult(Intent data) {
		String username = data.getStringExtra(EXTRA_USERNAME);
		String password = data.getStringExtra(EXTRA_PASSWORD);
		return new UsernamePasswordCredentials(username, password);
	}
}
