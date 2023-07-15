package com.example.pi_movil.geocode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Geocoding {

    public static String LatitudLongitud(String direccion) {
        String direccionCodificada = null;

        try {
            direccionCodificada = URLEncoder.encode(direccion, "UTF-8");

            String url = "https://nominatim.openstreetmap.org/search?q=" + direccionCodificada + "&format=json&addressdetails=1&limit=1";
            System.out.println(url);
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            double latitud = jsonObject.getDouble("lat");
            double longitud = jsonObject.getDouble("lon");

            String mensaje = latitud + "," + longitud;
            return mensaje;
            /*
            System.out.println("Latitud: " + latitud);
            System.out.println("Longitud: " + longitud);
             */

        } catch (Exception e) {
            return direccion;
        }

    }

    public static String obtenerNombre(String latitud, String longitud) {
        try {
            String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitud + "&lon=" + longitud;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            JSONObject json = new JSONObject(sb.toString());
            return json.getString("display_name");
        } catch (Exception e){
            return latitud+","+longitud;
        }

    }


}
