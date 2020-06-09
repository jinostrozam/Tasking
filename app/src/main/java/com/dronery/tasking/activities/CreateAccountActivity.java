package com.dronery.tasking.activities;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dronery.tasking.R;
import com.google.android.material.snackbar.Snackbar;

public class CreateAccountActivity extends DefaultActivity {
    private EditText email;
    private EditText password;
    private EditText confirm;
    private Button create;
    private ImageButton passwordShow;
    private ImageButton confirmShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void setLayout() {
        setContentView(R.layout.activity_create_account);
    }

    @Override
    public void createViews() {
        super.createViews();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        create = findViewById(R.id.create);
        passwordShow = findViewById(R.id.show_password);
        confirmShow = findViewById(R.id.show_confirm);
    }

    public void create(View view) {
        if(!validate(view)) return;

        setLoadingState(true);

        authManager.createAccount(thisActivity, email.getText().toString(), password.getText().toString(), task -> {
            if(!task.isSuccessful()) {
                Snackbar.make(view, R.string.create_error, Snackbar.LENGTH_LONG).show();
            } else {
                authManager.sendEmailVerification(thisActivity, value -> {
                    if(value.isSuccessful()) {
                        Snackbar.make(view, R.string.create_email_notification, Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, R.string.create_email_fail, Snackbar.LENGTH_LONG).show();
                    }
                });
                authManager.logout();
            }
            setLoadingState(false);
        });
    }

    public boolean validate(View view) {
        boolean valid = true;
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String confirmText = confirm.getText().toString();
        if(emailText.isEmpty()) {
            valid = false;
            email.setError(getString(R.string.create_error_no_email));
        }
        if(passwordText.isEmpty()) {
            valid = false;
            password.setError(getString(R.string.create_error_no_password));
        }
        if(confirmText.isEmpty()) {
            valid = false;
            confirm.setError(getString(R.string.create_error_no_confirmation));
        }
        if(!confirmText.isEmpty() && !passwordText.isEmpty() && !passwordText.equals(confirmText)) {
            valid = false;
            Snackbar.make(view, R.string.create_error_mismatch, Snackbar.LENGTH_LONG).show();
        }
        return valid;
    }

    public void showPassword(View view) {
        showPasswordLikeText(password);
    }

    public void showConfirm(View view) {
        showPasswordLikeText(confirm);
    }

    public void showPasswordLikeText(EditText passwordLike) {
        if(passwordLike.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            passwordLike.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordLike.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            passwordLike.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordLike.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        passwordLike.setSelection(passwordLike.getText().length());
    }
}