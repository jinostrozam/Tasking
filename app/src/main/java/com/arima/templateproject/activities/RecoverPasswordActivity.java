package com.arima.templateproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arima.templateproject.R;
import com.google.android.material.snackbar.Snackbar;

public class RecoverPasswordActivity extends DefaultActivity {
    private EditText email;
    private Button recover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_recover_password);
    }

    @Override
    public void createViews() {
        super.createViews();
        email = findViewById(R.id.email);
        recover = findViewById(R.id.recover);
    }

    public void recover(View view) {
        if(!validate()) return;

        setLoadingState(true);

        authManager.recoverPassword(email.getText().toString());

        setLoadingState(false);

        Snackbar.make(view, R.string.recover_sent, Snackbar.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;
        String emailText = email.getText().toString();
        if(emailText.isEmpty()) {
            valid = false;
            email.setError(getString(R.string.login_error_no_email));
        }
        return valid;
    }
}
