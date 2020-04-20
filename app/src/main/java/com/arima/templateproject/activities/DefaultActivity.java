package com.arima.templateproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.arima.templateproject.R;
import com.arima.templateproject.managers.FirebaseAuthManager;
import com.arima.templateproject.managers.FirestoreManager;
import com.arima.templateproject.managers.SharedPreferencesManager;

public abstract class DefaultActivity extends AppCompatActivity {
    protected DefaultActivity thisActivity;

    protected FirebaseAuthManager authManager;
    protected FirestoreManager firestoreManager;
    protected SharedPreferencesManager preferencesManager;

    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        createInternalItems();
        createViews();
    }

    abstract void setLayout();

    public void createInternalItems() {
        thisActivity = this;

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();
        preferencesManager = new SharedPreferencesManager(thisActivity);
    }

    public void createViews() {
        loadingPB = findViewById(R.id.loading);
    }

    public void setLoadingState(boolean loading) {
        setEnableViews(loading);

        loadingPB.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public void setEnableViews(boolean loading) {}
}
