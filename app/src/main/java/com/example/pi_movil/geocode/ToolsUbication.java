package com.example.pi_movil.geocode;

import java.util.ArrayList;
import java.util.List;

public class ToolsUbication {

    /**
     *
     Este método calcula la distancia entre dos puntos en la superficie de la Tierra utilizando la fórmula del haversine.

     En la primera línea, se establece una constante llamada RADIO_TIERRA con un valor de 6371 kilómetros.
     Este valor se utiliza en el cálculo posterior para convertir la distancia de radianes a kilómetros.

     Las siguientes dos líneas calculan la diferencia de latitud y longitud entre los dos puntos de entrada,
     convirtiendo los valores de grados a radianes utilizando el método toRadians de la clase Math.

     La tercera línea calcula la distancia angular (a) entre los dos puntos utilizando la fórmula del haversine.
     Esta línea utiliza las funciones seno y coseno para calcular a partir de las diferencias de latitud y longitud.

     La cuarta línea calcula la distancia en la superficie de la Tierra (c) entre los dos puntos utilizando la función atan2 de la clase Math.

     En la última línea, la distancia se calcula multiplicando el radio de la Tierra por la distancia angular.

     En resumen, el método calcula la distancia en kilómetros entre dos puntos en la superficie de la Tierra utilizando la fórmula del haversine.
     * */

    public static double distancia(double latitud1, double longitud1, double latitud2, double longitud2) {
        final double RADIO_TIERRA = 6371; // en kilómetros
        double dLat = Math.toRadians(latitud2 - latitud1);
        double dLon = Math.toRadians(longitud2 - longitud1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitud1)) * Math.cos(Math.toRadians(latitud2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = RADIO_TIERRA * c;
        return distancia;
    }

    public static List<Ubicacion> ubicacionesEnRadio(List<Ubicacion> objetos, double radioEnKm, double latitudCentral, double longitudCentral) {
        List<Ubicacion> objetosEnRadio = new ArrayList<>();
        for (Ubicacion ubica : objetosEnRadio) {
            double distancia = distancia(ubica.getLatitud(), ubica.getLongitud(), latitudCentral, longitudCentral);
            if (distancia <= radioEnKm) {
                objetosEnRadio.add(ubica);
            }
        }
        return objetosEnRadio;
    }

}
