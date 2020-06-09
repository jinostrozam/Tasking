package com.dronery.tasking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dronery.tasking.R;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends DefaultActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button create;
    private TextView recover;
    private ImageView showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void createViews() {
        super.createViews();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_ok);
        create = findViewById(R.id.login_create);
        recover = findViewById(R.id.label_recover);
        showPassword = findViewById(R.id.show_password);
    }

    public void login(View view) {
        if(!validate()) return;

        setLoadingState(true);

        authManager.login(thisActivity, email.getText().toString(), password.getText().toString(), task -> {
            if(!task.isSuccessful() || !authManager.isLogin()) {
                authManager.logout();
                Snackbar.make(view, R.string.login_error, Snackbar.LENGTH_LONG).show();
                setLoadingState(false);
                return;
            }
            if(authManager.isLogin()) {
                redirectToMain();
            }
        });
    }

    public void create(View view) {
        redirectToCreate();
    }

    public void recover(View view) {
        redirectToRecover();
    }

    public boolean validate() {
        boolean valid = true;
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if(emailText.isEmpty()) {
            valid = false;
            email.setError(getString(R.string.login_error_no_email));
        }
        if(passwordText.isEmpty()) {
            valid = false;
            password.setError(getString(R.string.login_error_no_password));
        }
        return valid;
    }

    private void redirectToMain() {
        Intent intent = new Intent(thisActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToCreate() {
        Intent intent = new Intent(thisActivity, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void redirectToRecover() {
        Intent intent = new Intent(thisActivity, RecoverPasswordActivity.class);
        startActivity(intent);
    }

    public void showPassword(View view) {
        showPasswordLikeText(password);
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
