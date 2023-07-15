package com.example.pi_movil.datos;

import com.example.pi_movil.geocode.Direccion;

public class Partido {

    private double latitud;
    private double longitud;
    private double coste;
    private String horaInicio;
    private String fechaInicio;
    private String horaLimite;
    private String fechaLimite;
    private String deporte;
    private String estado;
    private int inscritos;
    private int maxInscritos;
    private int id;
    private Direccion ubicacion;

    public Partido() {
    }

    public Partido(double latitud, double longitud, double coste, String horaInicio, String fechaInicio, String horaLimite, String fechaLimite, String deporte, String estado, int inscritos, int maxInscritos, int id) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.coste = coste;
        this.horaInicio = horaInicio;
        this.fechaInicio = fechaInicio;
        this.horaLimite = horaLimite;
        this.fechaLimite = fechaLimite;
        this.deporte = deporte;
        this.estado = estado;
        this.inscritos = inscritos;
        this.maxInscritos = maxInscritos;
        this.id = id;
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

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getHoraLimite() {
        return horaLimite;
    }

    public void setHoraLimite(String horaLimite) {
        this.horaLimite = horaLimite;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getInscritos() {
        return inscritos;
    }

    public void setInscritos(int inscritos) {
        this.inscritos = inscritos;
    }

    public int getMaxInscritos() {
        return maxInscritos;
    }

    public void setMaxInscritos(int maxInscritos) {
        this.maxInscritos = maxInscritos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Direccion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Direccion ubicacion) {
        this.ubicacion = ubicacion;
    }

}
