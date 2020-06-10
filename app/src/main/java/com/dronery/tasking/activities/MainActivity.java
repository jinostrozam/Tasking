package com.dronery.tasking.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.dronery.tasking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;

public class MainActivity extends DefaultActivity {
    private FloatingActionButton mail;
    private AppBarConfiguration mAppBarConfiguration;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_example, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        builder = new AlertDialog.Builder(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateLogin();
    }

    @Override
    void setLayout() {
        setContentView(R.layout.activity_main);
    }

//    @Override
//    public void createViews() {
//        super.createViews();
//        mail = findViewById(R.id.mail);
//        mail.setOnClickListener(view -> showEmail(mail  ));
//    }

    public void showEmail(View view) {
        Snackbar.make(view, authManager.getUser().getEmail(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void validateLogin() {
        try{
            if(!authManager.isLogin()) {
                logout();
            }
            authManager.reload(thisActivity, task -> {
                if(!task.isSuccessful() || !authManager.isLogin()) {
                    logout();
                }
            });

            //throw new Exception("cai");
        }catch(Throwable e){
            e.printStackTrace();
            builder.setTitle(R.string.alert_header).setMessage(R.string.alert_error_validate_login)
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
                getApplicationContext().getResources().getString(R.string.code_email_MainActivity));
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

    public void logout() {
        authManager.logout();
        preferencesManager.clearSharedPreferences();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(thisActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
