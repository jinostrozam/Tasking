package com.arima.templateproject.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
        if(loading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        loadingPB.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(loadingPB != null) {
            loadingPB.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
