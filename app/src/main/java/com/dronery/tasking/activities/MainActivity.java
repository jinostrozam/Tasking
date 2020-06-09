package com.dronery.tasking.activities;

import android.content.Intent;
import android.os.Bundle;

import com.dronery.tasking.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

public class MainActivity extends DefaultActivity {
    private FloatingActionButton mail;
    private AppBarConfiguration mAppBarConfiguration;

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

    @Override
    public void createViews() {
        super.createViews();
        mail = findViewById(R.id.mail);
        mail.setOnClickListener(view -> showEmail(mail  ));
    }

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
        if(!authManager.isLogin()) {
            logout();
        }
        authManager.reload(thisActivity, task -> {
            if(!task.isSuccessful() || !authManager.isLogin()) {
                logout();
            }
        });
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
