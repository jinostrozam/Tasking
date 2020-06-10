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
import android.widget.ImageButton;

import com.dronery.tasking.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CreateAccountActivity extends DefaultActivity {
    private EditText email;
    private EditText password;
    private EditText confirm;
    private Button create;
    private ImageButton passwordShow;
    private ImageButton confirmShow;
    private AlertDialog.Builder builder;

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
        builder = new AlertDialog.Builder(CreateAccountActivity.this);
    }

    public void create(View view) {
        if(!validate(view)) return;

        setLoadingState(true);
        try{
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
        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_create_account)
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
                getApplicationContext().getResources().getString(R.string.error_email_CreateAccountActivity));
        intent.setType("text/plain");
        final PackageManager pm = getApplicationContext().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(intent);
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