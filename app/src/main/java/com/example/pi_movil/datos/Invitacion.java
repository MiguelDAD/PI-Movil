package com.example.pi_movil.datos;

public class Invitacion {

    private int id;
    private String remitente;
    private String tipo;


    public Invitacion(int id, String remitente) {
        this.id = id;
        this.remitente = remitente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String mensaje) {
        this.tipo = mensaje;
    }

}
