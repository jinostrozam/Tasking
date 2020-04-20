package com.arima.templateproject.model;

import com.google.firebase.firestore.PropertyName;

public class Example {
    private String exampleField;

    @PropertyName("example_field")
    public String getExampleField() {
        return exampleField;
    }

    @PropertyName("example_field")
    public void setExampleField(String exampleField) {
        this.exampleField = exampleField;
    }
}
