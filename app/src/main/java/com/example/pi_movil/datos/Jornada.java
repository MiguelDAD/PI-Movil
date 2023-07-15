package com.example.pi_movil.datos;

public class Jornada {
    //id;eLocal;ptosLocal;eVisitante;ptosVisiante;fecha;hora!

    private int id;
    private String equipoLocal;
    private String ptosLocal;
    private String equipoVisitante;
    private String ptosVisitante;
    private String fecha;
    private String hora;

    public Jornada() {
    }

    public Jornada(int id, String equipoLocal, String ptosLocal, String equipoVisitante, String ptosVisitante, String fecha, String hora) {
        this.id = id;
        this.equipoLocal = equipoLocal;
        this.ptosLocal = ptosLocal;
        this.equipoVisitante = equipoVisitante;
        this.ptosVisitante = ptosVisitante;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getPtosLocal() {
        return ptosLocal;
    }

    public void setPtosLocal(String ptosLocal) {
        this.ptosLocal = ptosLocal;
    }

    public String getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(String equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getPtosVisitante() {
        return ptosVisitante;
    }

    public void setPtosVisitante(String ptosVisitante) {
        this.ptosVisitante = ptosVisitante;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}