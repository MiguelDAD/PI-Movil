package com.example.pi_movil.geocode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrdenarPorCercania implements Comparator<Ubicacion> {
    private double latitudActual;
    private double longitudActual;

    public OrdenarPorCercania(double latitudActual, double longitudActual) {
        this.latitudActual = latitudActual;
        this.longitudActual = longitudActual;
    }

    public int compare(Ubicacion o1, Ubicacion o2) {


        double distancia1 = ToolsUbication.distancia(o1.getLatitud(), o1.getLongitud(), latitudActual, longitudActual);
        double distancia2 = ToolsUbication.distancia(o2.getLatitud(), o2.getLongitud(), latitudActual, longitudActual);
        return Double.compare(distancia1, distancia2);
    }
}
