package com.arima.templateproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arima.templateproject.R;
import com.arima.templateproject.managers.FirebaseAuthManager;
import com.arima.templateproject.managers.FirestoreManager;
import com.arima.templateproject.managers.SharedPreferencesManager;

public abstract class DefaultFragment extends Fragment {
    protected DefaultFragment thisFragment;

    protected FirebaseAuthManager authManager;
    protected FirestoreManager firestoreManager;
    protected SharedPreferencesManager preferencesManager;

    protected ProgressBar loadingPB;

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

    public void createViewItems(View root) {
        loadingPB = root.findViewById(R.id.loading);
    }

    public abstract View setFragmentLayout(LayoutInflater inflater, ViewGroup container);

    public void setLoadingState(boolean loading) {
        if(loading) {
            thisFragment.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            thisFragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        loadingPB.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
