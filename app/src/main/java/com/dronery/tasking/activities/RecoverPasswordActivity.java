package com.dronery.tasking.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dronery.tasking.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecoverPasswordActivity extends DefaultActivity {
    private EditText email;
    private Button recover;
    private AlertDialog.Builder builder;

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
        builder = new AlertDialog.Builder(RecoverPasswordActivity.this);
    }

    public void recover(View view) {
        if(!validate()) return;

        setLoadingState(true);
        try{
            authManager.recoverPassword(email.getText().toString());

            setLoadingState(false);

            Snackbar.make(view, R.string.recover_sent, Snackbar.LENGTH_LONG).show();
        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_recover_password)
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
                getApplicationContext().getResources().getString(R.string.error_email_RecoverPasswordActivity));
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
