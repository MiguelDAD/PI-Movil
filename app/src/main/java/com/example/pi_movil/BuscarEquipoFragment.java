package com.example.pi_movil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.geocode.Distancia;
import com.example.pi_movil.tarjetas.EquiposAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class BuscarEquipoFragment extends Fragment {

    MapView mapa;
    Distancia actual;
    GoogleMap gmap;
    SeekBar barraDistancia;
    FusedLocationProviderClient client;
    String posicionActual;
    Circle circulo;
    List<Equipo> equipos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Permite que el fragmento maneje el ciclo de vida del mapa.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buscar_equipo, container, false);

        barraDistancia = view.findViewById(R.id.seekBarEquipos);

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        posicionActual = "";
        obtenerDireccion();

        actual = Distancia.CERCA;
        barraDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (i) {
                    case 0:
                        actual = Distancia.MUYCERCA;
                        break;
                    case 1:
                        actual = Distancia.CERCA;
                        break;
                    case 2:
                        actual = Distancia.NORMAL;
                        break;
                    case 3:
                        actual = Distancia.PROXIMO;
                        break;
                    case 4:
                        actual = Distancia.LEJOS;
                        break;
                    case 5:
                        actual = Distancia.MUYLEJOS;
                        break;
                    case 6:
                        actual = Distancia.SINLIMITE;
                        break;
                    default:
                        return;
                }

                obtenerDireccion();

                rellenarMapa();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mapa = view.findViewById(R.id.mapViewEquipos);
        mapa.onCreate(savedInstanceState);
        mapa.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;

                // Verifica si se tienen los permisos de ubicación.
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gmap.setMyLocationEnabled(true);
                }

                // Obtiene la última ubicación conocida.
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                            // Mueve la cámara al punto deseado y establece el nivel de zoom.
                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f));
                           try{
                                circulo = gmap.addCircle(new CircleOptions().
                                        center(latLng).
                                        radius(actual.getKms()*1000).
                                        strokeWidth(2).
                                        strokeColor(Color.parseColor("#D500FF")).
                                        fillColor(Color.parseColor("#44FF21F8")));
                            }catch (Exception e){
                               System.out.println("MAPA RADIO NEGATIVO");
                           }

                            obtenerPosicionActual();
                            rellenarMapa();

                        }
                    }
                });
                gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(@NonNull Marker marker) {
                        String nombre = marker.getTitle();
                        for(Equipo e : equipos){
                            if(e.getNombreEquipo().equalsIgnoreCase(nombre)){
                                EquipoInformacionFragment nuevoFragmento = new EquipoInformacionFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, nuevoFragmento);
                                transaction.addToBackStack(null);
                                nuevoFragmento.cargarEquipoNoInscrito(e);
                                transaction.commit();
                                return;
                            }
                        }
                    }
                });
            }
        });


        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //COMPRUEBO SI HA ACEPTADO EL PERMISO DE GPS
        if (requestCode == 100 && (grantResults.length>0) &&
                (grantResults[0]+grantResults[1]== PackageManager.PERMISSION_GRANTED)){
            //SI ACEPTA EL PERMISO DE GPS
            obtenerPosicionActual();
        }else{
            //SI NO ACEPTA EL PERMISO
            Toast.makeText(getActivity(),"SIN SU UBICACION NO PODEMOS MOSTRARLE LOS TORNEOS CERCANOS",Toast.LENGTH_SHORT);
        }
    }

    //OBTIENE DIRECCION Y BUSCA LOS TORNEOS CERCANOS
    private void obtenerDireccion(){
        //MIRAR SI HA PERMITIDO LA UBICACION
        if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            obtenerPosicionActual();
        }else{
            //SI NO TIENE PERMISOS DE UBICACION SOLICITA PERMISOS
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }

    @SuppressLint("MissingPermission")
    private void obtenerPosicionActual(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //SI EL GPS ESTA ACTIVADO
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location!=null){
                        //Obtengo la latitud y la longitud;
                        posicionActual = location.getLatitude()+","+location.getLongitude();
                    } else{

                        //Inicializo una solicitud de localizacion
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                posicionActual = location1.getLatitude()+","+location1.getLongitude();

                            }
                        };

                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());


                    }
                }
            });
        }else{
            //SI NO ME DA LOS PERMISOS DE LOCALIZACION SE ABRE LA CONFIGRACION DE LOCALIZACION
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void rellenarMapa(){
        gmap.clear();
        if(posicionActual.equalsIgnoreCase("")){
            return;
        }

        String peticion = Mensajes.SEARCH_TEAMS+";"+posicionActual+";"+actual.getKms();

        BuscarEquipoFragment.Tasks nTasks = new BuscarEquipoFragment.Tasks();
        nTasks.execute(peticion);
        try {
            if (actual.getKms() != -1) {
                if (circulo != null) {
                    circulo.remove();
                    String[] partesubi = posicionActual.replaceAll(" ", "").split(",");
                    LatLng latLng = new LatLng(Double.parseDouble(partesubi[0]), Double.parseDouble(partesubi[1]));

                    circulo = gmap.addCircle(new CircleOptions().
                            center(latLng).
                            radius(actual.getKms() * 1000).
                            strokeWidth(2).
                            strokeColor(Color.parseColor("#D500FF")).
                            fillColor(Color.parseColor("#44FF21F8")));
                }
            }
        }catch (Exception e){
            System.out.println("Errror en la construcción del circulo");
        }
    }

    class Tasks extends AsyncTasks<String, Float, String> {
        Socket s;
        PrintWriter out = null;
        BufferedReader in = null;

        @Override
        public void onPreExecute() {
            s = Session.getSocket();
            ((HomeActivity)getActivity()).cargando();
        }

        @Override
        public String doInBackground(String... strings) {
            Log.v("Enviar: ", strings[0]);

            String recibido = "";
            try {
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.println(strings[0]);
                try {
                    recibido = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.v("Recibido del servidor: ", recibido);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "El servidor esta offline", Toast.LENGTH_SHORT).show();
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.SEARCH_TEAMS_OK)) {
                equipos = new LinkedList<>();
                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesEquipos = partesRespuesta[i].split(":");
                    String nombreEqupo = partesEquipos[0];
                    String deporte = partesEquipos[1];
                    String lider = partesEquipos[2];
                    String[] integrantes = partesEquipos[3].split("!");
                    String[] ubicacion = partesEquipos[4].replaceAll(" ","").split(",");
                    String privacidad = partesEquipos[5];
                    double latitud = Double.parseDouble(ubicacion[0]);
                    double longitud = Double.parseDouble(ubicacion[1]);


                    Log.i("EQUIPO " + i + ": ", nombreEqupo + " " + lider + " " + partesEquipos[2]);

                    Equipo e = new Equipo(nombreEqupo, deporte, lider);
                    e.setLatitud(latitud);
                    e.setLongitud(longitud);
                    e.setDireccion(new Direccion(getContext(),latitud,longitud));
                    e.setPrivacidad(privacidad);

                    for (String participante : integrantes) {
                        e.insertarUsuario(participante);
                    }

                    equipos.add(e);
                }

               for(Equipo e : equipos){
                   LatLng posicion = new LatLng(e.getLatitud(),e.getLongitud());

                   MarkerOptions markerOptions = new MarkerOptions()
                           .position(posicion)
                           .title(e.getNombreEquipo())
                           .snippet(e.getDeporte());

                   gmap.addMarker(markerOptions);


               }

            } else {
                Toast.makeText(getActivity(), "No se pueden obtener los equipos", Toast.LENGTH_SHORT).show();
            }
            ((HomeActivity)getActivity()).completado();

        }
    }

    @Override
    public void onResume() {
        mapa.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapa.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapa.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapa.onLowMemory();
    }
}