package com.example.pi_movil.geocode;

public enum Distancia {

    MUYCERCA("2",2),CERCA("5",5),NORMAL("10",10),PROXIMO("25",25),
    LEJOS("50",50), MUYLEJOS("100",100),SINLIMITE("Sin Limite",-1);

    private String cantidad;
    private int kms;

    Distancia(String cantidad, int kms) {
        this.cantidad = cantidad;
        this.kms = kms;
    }

    public String getCantidad() {
        return cantidad;
    }

    public int getKms() {
        return kms;
    }
}
