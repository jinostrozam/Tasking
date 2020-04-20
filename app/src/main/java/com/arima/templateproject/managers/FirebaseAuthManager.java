package com.arima.templateproject.managers;

import android.app.Activity;

import com.arima.templateproject.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {
    private FirebaseAuth auth;

    public FirebaseAuthManager() {
        auth = FirebaseAuth.getInstance();
    }

    public boolean isLogin() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser != null && currentUser.isEmailVerified();
    }

    public void login(Activity context, String email, String password, OnCompleteListener<AuthResult> responseHandler) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(context, responseHandler);
    }

    public void createAccount(Activity context, String email, String password, OnCompleteListener<AuthResult> responseHandler) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(context, responseHandler);
    }

    public void sendEmailVerification(Activity context, OnCompleteListener<Void> responseHandler) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null && !currentUser.isEmailVerified()) {
            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(context, responseHandler);
        }
    }

    public void reload(Activity context, OnCompleteListener<Void> responseHandler) {
        auth.getCurrentUser().reload().addOnCompleteListener(context, responseHandler);
    }

    public void recoverPassword(String email) {
        auth.sendPasswordResetEmail(email);
    }

    public User getUser() {
        if(!isLogin()) {
            return null;
        }
        User user = new User();
        FirebaseUser currentUser = auth.getCurrentUser();
        user.setId(currentUser.getUid());
        user.setEmail(currentUser.getEmail());
        return user;
    }

    public void logout() {
        this.auth.signOut();
    }
}
