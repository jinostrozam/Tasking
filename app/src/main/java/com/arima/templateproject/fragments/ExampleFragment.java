package com.arima.templateproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arima.templateproject.R;
import com.arima.templateproject.managers.FirestoreManager;
import com.arima.templateproject.model.Example;

public class ExampleFragment extends DefaultFragment {
    TextView exampleText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        loadExampleTexts();
        return root;
    }

    @Override
    public View setFragmentLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void createViewItems(View root) {
        exampleText = root.findViewById(R.id.example);
    }

    public void loadExampleTexts() {
        firestoreManager.getDocument(
                FirestoreManager.FS_COLLECTION_EXAMPLE,
                FirestoreManager.FS_DOCUMENT_EXAMPLE,
                documentSnapshot -> {
                    if(!documentSnapshot.isSuccessful()) {
                        exampleText.setText("---");
                        return;
                    }
                    Example object = documentSnapshot.getResult().toObject(Example.class);
                    exampleText.setText(object.getExampleField());
                });
    }
}