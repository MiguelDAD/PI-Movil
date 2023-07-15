package com.example.pi_movil.geocode;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Direccion implements Ubicacion {

    private Geocoder geocoder;
    private String calle;
    private String ciudad;
    private String provincia;
    private String pais;
    private String codigoPostal;

    private double latitud;
    private double longitud;



    public Direccion(Context context, double latitud, double longitud){
        try {
            geocoder = new Geocoder(context, Locale.getDefault());

            this.latitud = latitud;
            this.longitud = longitud;


            List<Address> direccionesProximas = geocoder.getFromLocation(latitud,longitud,1);
            calle = direccionesProximas.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            ciudad = direccionesProximas.get(0).getLocality();
            provincia = direccionesProximas.get(0).getAdminArea();
            pais = direccionesProximas.get(0).getCountryName();
            codigoPostal = direccionesProximas.get(0).getPostalCode();

        } catch (Exception e) {
            Toast.makeText(context,"NO SE PUDO OBTENER LA UBICACION",Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            calle = "";
            ciudad = "";
            provincia = "";
            pais = "";
            codigoPostal = "";
        }


    }

    public Geocoder getGeocoder() {
        return geocoder;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
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

    @NonNull
    @Override
    public String toString() {
        return ciudad+", "+provincia+": "+calle+", "+codigoPostal+". "+pais;
    }
}
