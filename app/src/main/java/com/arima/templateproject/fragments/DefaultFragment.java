package com.arima.templateproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arima.templateproject.managers.FirebaseAuthManager;
import com.arima.templateproject.managers.FirestoreManager;
import com.arima.templateproject.managers.SharedPreferencesManager;

public abstract class DefaultFragment extends Fragment {
    protected DefaultFragment thisFragment;

    protected FirebaseAuthManager authManager;
    protected FirestoreManager firestoreManager;
    protected SharedPreferencesManager preferencesManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = setFragmentLayout(inflater, container);

        createInternalItems();
        createViewItems(root);

        return root;
    }

    public void createInternalItems() {
        thisFragment = this;

        authManager = new FirebaseAuthManager();
        firestoreManager = new FirestoreManager();
        preferencesManager = new SharedPreferencesManager(getActivity());
    }

    public void createViewItems(View root) {}

    public abstract View setFragmentLayout(LayoutInflater inflater, ViewGroup container);
}
