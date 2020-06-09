package com.arima.templateproject.model;

import com.google.firebase.firestore.PropertyName;

import java.util.List;

public class Actividades {
    private List<String> textos;
    private List<Boolean> estado;

    @PropertyName("textos")
    public List<String> getTextos() {
        return textos;
    }

    @PropertyName("textos")
    public void setTextos(List<String> textos) {
        this.textos = textos;
    }

    @PropertyName("estados")
    public List<Boolean> getEstado() {
        return estado;
    }

    @PropertyName("estados")
    public void setEstado(List<Boolean> estado) {this.estado = estado;}
}


