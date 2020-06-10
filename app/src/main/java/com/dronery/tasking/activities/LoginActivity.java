package com.dronery.tasking.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dronery.tasking.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static java.security.AccessController.getContext;

public class LoginActivity extends DefaultActivity {
    private EditText email;
    private EditText password;
    private Button login;
    private Button create;
    private TextView recover;
    private ImageView showPassword;
    private AlertDialog.Builder builder;

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
        builder = new AlertDialog.Builder(LoginActivity.this);
    }

    public void login(View view) {
        if(!validate()) return;

        setLoadingState(true);

        try{
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

        }catch(Throwable e){
            e.printStackTrace();

            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_login)
                    .setNeutralButton(R.string.alert_button_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            testEmail();
                            dialog.cancel();

                        }
                    })
            .show();
        }
    }

    public void testEmail(){
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ getApplicationContext().getResources().getString(R.string.alert_mail_dev) });
        intent.putExtra(Intent.EXTRA_SUBJECT, getApplicationContext().getResources().getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getApplicationContext().getResources().getString(R.string.email_body) +
                getApplicationContext().getResources().getString(R.string.code_email_LoginActivity));
        intent.setType("text/plain");
        final PackageManager pm = getApplication().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(intent);
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
