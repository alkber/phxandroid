package org.phxandroid.examples.dialogs;

import org.phxandroid.examples.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CredentialsActivity extends Activity {
    private EditText username;
    private EditText password;
    private TextView realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credentials);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        realm = (TextView) findViewById(R.id.realm);

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
        intent.putExtra("username", username.getText().toString());
        intent.putExtra("password", password.getText().toString());
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String realmText = bundle.getString("realm");
            // TODO: handle scheme
            // TODO: handle host
            realm.setText(realmText);
        }
    }
}
