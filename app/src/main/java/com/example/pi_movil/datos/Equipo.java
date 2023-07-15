package com.example.pi_movil.datos;

import com.example.pi_movil.geocode.Direccion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Equipo implements Serializable {
    private String nombreEquipo;
    private String deporte;
    private String lider;
    private List<String> integrantes;
    private double latitud, longitud;
    private Direccion direccion;
    private String privacidad;


    public Equipo(String nombreEquipo, String deporte, String lider) {
        this.nombreEquipo = nombreEquipo;
        this.deporte = deporte;
        this.lider = lider;
        integrantes = new ArrayList<>();
        direccion = null;
    }

    public String getPrivacidad() {
        return privacidad;
    }

    public void setPrivacidad(String privacidad) {
        this.privacidad = privacidad;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getLider() {
        return lider;
    }

    public void setLider(String lider) {
        this.lider = lider;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<String> integrantes) {
        this.integrantes = integrantes;
    }

    public void insertarUsuario (String usuario){
        integrantes.add(usuario);
    }


}