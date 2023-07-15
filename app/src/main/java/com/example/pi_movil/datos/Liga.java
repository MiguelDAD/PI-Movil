package com.example.pi_movil.datos;

import com.example.pi_movil.geocode.Direccion;

public class Liga {

    private int id;
    private String nombre;
    private double latitud;
    private double longitud;
    private double coste;
    private int maxEquipos;
    private int minEquipos;
    private String horaInicio;
    private String fechaInicio;
    private String horaLimite;
    private String fechaLimite;
    private String deporte;
    private int frecuenciaJornada;
    private int duracionPartidos;
    private String hInicioPartidos;
    private String hFinPartidos;
    private String estado;
    private int equiposInscritos;
    private Direccion ubicacion;

    public Liga() {
    }

    public Liga(int id, String nombre, double latitud, double longitud, double coste, int maxEquipos, int minEquipos, String horaInicio, String fechaInicio, String horaLimite, String fechaLimite, String deporte, int frecuenciaJornada, int duracionPartidos, String hInicioPartidos, String hFinPartidos, String estado, int equiposInscritos) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.coste = coste;
        this.maxEquipos = maxEquipos;
        this.minEquipos = minEquipos;
        this.horaInicio = horaInicio;
        this.fechaInicio = fechaInicio;
        this.horaLimite = horaLimite;
        this.fechaLimite = fechaLimite;
        this.deporte = deporte;
        this.frecuenciaJornada = frecuenciaJornada;
        this.duracionPartidos = duracionPartidos;
        this.hInicioPartidos = hInicioPartidos;
        this.hFinPartidos = hFinPartidos;
        this.estado = estado;
        this.equiposInscritos = equiposInscritos;
    }

    public void nuevaInscripccion(){
        equiposInscritos++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {
        this.maxEquipos = maxEquipos;
    }

    public int getMinEquipos() {
        return minEquipos;
    }

    public void setMinEquipos(int minEquipos) {
        this.minEquipos = minEquipos;
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

    public int getFrecuenciaJornada() {
        return frecuenciaJornada;
    }

    public void setFrecuenciaJornada(int frecuenciaJornada) {
        this.frecuenciaJornada = frecuenciaJornada;
    }

    public int getDuracionPartidos() {
        return duracionPartidos;
    }

    public void setDuracionPartidos(int duracionPartidos) {
        this.duracionPartidos = duracionPartidos;
    }

    public String gethInicioPartidos() {
        return hInicioPartidos;
    }

    public void sethInicioPartidos(String hInicioPartidos) {
        this.hInicioPartidos = hInicioPartidos;
    }

    public String gethFinPartidos() {
        return hFinPartidos;
    }

    public void sethFinPartidos(String hFinPartidos) {
        this.hFinPartidos = hFinPartidos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getEquiposInscritos() {
        return equiposInscritos;
    }

    public void setEquiposInscritos(int equiposInscritos) {
        this.equiposInscritos = equiposInscritos;
    }

    public Direccion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Direccion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String inscritosMaxInscritos(){
        return equiposInscritos+"/"+maxEquipos;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
