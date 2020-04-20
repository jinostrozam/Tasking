package com.arima.templateproject.activities;

import android.content.Intent;
import android.os.Bundle;

import com.arima.templateproject.managers.SharedPreferencesManager;
import com.arima.templateproject.model.User;

public class SplashActivity extends DefaultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserAndRedirect();
    }

    @Override
    public void setLayout() {}

    private void checkUserAndRedirect() {
        if(!authManager.isLogin()) {
            invalidLogin(false);
            return;
        }
        User currentUser = authManager.getUser();
        preferencesManager.savePreference(SharedPreferencesManager.SP_USER, currentUser);

        // Load your default values here

        redirectToMain();
    }

    private void invalidLogin(boolean logout) {
        if(logout) {
            authManager.logout();
        }
        preferencesManager.clearSharedPreferences();
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(thisActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToMain() {
        Intent intent = new Intent(thisActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
