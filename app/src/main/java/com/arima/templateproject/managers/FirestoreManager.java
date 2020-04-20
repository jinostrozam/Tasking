package com.arima.templateproject.managers;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreManager {
    //Firestore collections
    public static final String FS_COLLECTION_EXAMPLE = "example_collection";

    //Firestore documents
    public static final String FS_DOCUMENT_EXAMPLE = "example_document";

    //Firestore fields
    public static final String FS_FIELD_EXAMPLE = "example_field";

    private FirebaseFirestore db;

    public FirestoreManager() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getCollection(String collectionPath, OnCompleteListener<QuerySnapshot> responseHandler){
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(responseHandler);
    }

    public void getCollection(String collectionPath, OnSuccessListener<QuerySnapshot> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .get()
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void getCollectionWhereEqualsTo(String collectionPath, String filterField, String filterValue, OnSuccessListener<QuerySnapshot> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .whereEqualTo(filterField, filterValue)
                .get()
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void getCollectionWhereEqualsTo(String collectionPath, String filterField, String filterValue, OnCompleteListener<QuerySnapshot> responseHandler){
        db.collection(collectionPath)
                .whereEqualTo(filterField, filterValue)
                .get()
                .addOnCompleteListener(responseHandler);
    }

    public void getDocument(String collectionPath, String documentKey, OnCompleteListener<DocumentSnapshot> responseHandler) {
        db.collection(collectionPath)
                .document(documentKey)
                .get()
                .addOnCompleteListener(responseHandler);
    }

    public void getDocument(String collectionPath, String documentKey, OnSuccessListener<DocumentSnapshot> successHandler, OnFailureListener failureHandler) {
        db.collection(collectionPath)
                .document(documentKey)
                .get()
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void saveObject(String collectionPath, String documentKey, Object objectToSave, OnCompleteListener<Void> responseHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .set(objectToSave)
                .addOnCompleteListener(responseHandler);
    }

    public void saveObject(String collectionPath, String documentKey, Object objectToSave, OnSuccessListener<Void> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .set(objectToSave)
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void setField(String collectionPath, String documentKey, String field, Object fieldValue, OnCompleteListener<Void> responseHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .update(field, fieldValue)
                .addOnCompleteListener(responseHandler);
    }

    public void setField(String collectionPath, String documentKey, String field, Object fieldValue, OnSuccessListener<Void> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .update(field, fieldValue)
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void setFields(String collectionPath, String documentKey, Map<String, Object> fieldMap, OnCompleteListener<Void> responseHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .update(fieldMap)
                .addOnCompleteListener(responseHandler);
    }

    public void setFields(String collectionPath, String documentKey, Map<String, Object> fieldMap, OnSuccessListener<Void> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .document(documentKey)
                .update(fieldMap)
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }

    public void addDocument(String collectionPath, Object documentToSave, OnCompleteListener<DocumentReference> responseHandler){
        db.collection(collectionPath)
                .add(documentToSave)
                .addOnCompleteListener(responseHandler);
    }

    public void addDocument(String collectionPath, Object documentToSave, OnSuccessListener<DocumentReference> successHandler, OnFailureListener failureHandler){
        db.collection(collectionPath)
                .add(documentToSave)
                .addOnSuccessListener(successHandler)
                .addOnFailureListener(failureHandler);
    }
}
