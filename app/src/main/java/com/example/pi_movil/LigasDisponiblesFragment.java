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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.datos.Partido;
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.dialogos.FiltrarDialogo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.geocode.Distancia;
import com.example.pi_movil.tarjetas.LigaAdapter;
import com.example.pi_movil.tarjetas.TorneoAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class LigasDisponiblesFragment extends Fragment implements FiltrarDialogo.FiltradoDatos {

    TextView kmDeTi;
    SeekBar barraDistancia;
    RecyclerView tusLigas;
    Distancia actual;

    String posicionActual;
    List<Liga> ligas;

    ImageView filtrado;
    MapView mapa;
    GoogleMap gmap;
    Circle circulo;
    FloatingActionButton botonFlotante;
    FusedLocationProviderClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ligas_disponibles, container, false);

        kmDeTi = view.findViewById(R.id.kilometrosDeTiLiga);
        barraDistancia = view.findViewById(R.id.seekBarLiga);
        tusLigas = view.findViewById(R.id.ligasDisponibles);

        actual = Distancia.NORMAL;
        ligas = new LinkedList<>();

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        posicionActual = "";
        obtenerDireccion();

        botonFlotante = view.findViewById(R.id.alternarMapaLiga);
        botonFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intercambiarVista();
            }
        });

        barraDistancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                switch (i){
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
                kmDeTi.setText(actual.getCantidad());
                obtenerDireccion();

                if(mapa.getVisibility()==View.VISIBLE){
                    rellenarMapa();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        filtrado = view.findViewById(R.id.buscarLiga);
        filtrado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarDialogo();
            }
        });

        mapa = view.findViewById(R.id.ligasMapView);
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
                        String id = marker.getTitle().substring(marker.getTitle().indexOf(":")+1);
                        for(Liga e : ligas){
                            if(id.equalsIgnoreCase(e.getId()+"")){
                                InformacionLigaFragment nuevoFragmento = new InformacionLigaFragment(e);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame_layout, nuevoFragmento);
                                transaction.addToBackStack(null);
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

    private void intercambiarVista(){

        if(mapa.getVisibility()==View.GONE){
            tusLigas.setVisibility(View.GONE);
            mapa.setVisibility(View.VISIBLE);
            botonFlotante.setImageResource(R.drawable.baseline_list_24);
            rellenarMapa();
        }else{
            tusLigas.setVisibility(View.VISIBLE);
            mapa.setVisibility(View.GONE);
            botonFlotante.setImageResource(R.drawable.baseline_map_24);
            circulo.remove();
            gmap.clear();
        }

    }

    private void rellenarMapa(){
        try {
            gmap.clear();
            if(posicionActual.equalsIgnoreCase("")){
                return;
            }
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

        for(Liga e : ligas){
            LatLng posicion = new LatLng(e.getLatitud(),e.getLongitud());

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(posicion)
                    .title(e.getNombre()+"\n:"+e.getId())
                    .snippet(e.getDeporte());

            gmap.addMarker(markerOptions);

        }
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
                        //LLAMO A LA TAREA DE MOSTRAR LOS TORNEOS CERCANOS
                        actualizarTorneos();
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
                                actualizarTorneos();

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

    public void actualizarTorneos(){

        String peticion = Mensajes.LIGUES_AVAILABLE+";"+posicionActual+";"+actual.getKms();

        LigasDisponiblesFragment.Tasks nTasks = new LigasDisponiblesFragment.Tasks();
        nTasks.execute(peticion);

    }

    @Override
    public void filtrarDatos(String usuario) {
        String[] partesFiltrado = usuario.split(";");
        String deporte = partesFiltrado[0];
        String fechaInicioString = partesFiltrado[1];
        String fechaFinString = partesFiltrado[2];

        if(!deporte.equalsIgnoreCase(" ")){
            List<Liga> eliminar = new LinkedList<>();
            for(int i = 0; i<ligas.size(); i++){
                Liga p = ligas.get(i);
                if(!p.getDeporte().equalsIgnoreCase(deporte)){
                    eliminar.add(p);
                }
            }
            ligas.removeAll(eliminar);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            List<Liga> eliminar = new LinkedList<>();
            for(int i = 0; i<ligas.size(); i++){
                Liga p = ligas.get(i);
                if(!fechaInicioString.equalsIgnoreCase(" ")&& !fechaFinString.equalsIgnoreCase(" ")){
                    Date fechaInicio = dateFormat.parse(fechaInicioString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fechaInicio);
                    // Restar un día
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    // Obtener la fecha resultante
                    fechaInicio = calendar.getTime();

                    Date fechaFin = dateFormat.parse(fechaFinString);
                    calendar.setTime(fechaFin);
                    // Restar un día
                    calendar.add(Calendar.DAY_OF_YEAR, +1);
                    // Obtener la fecha resultante
                    fechaFin = calendar.getTime();

                    String fechaPartidoString = p.getFechaInicio();
                    Date fechaPartido= dateFormat2.parse(fechaPartidoString);

                    if (!(fechaPartido.after(fechaInicio) && fechaPartido.before(fechaFin))) {
                        eliminar.add(p);
                    }
                }else if(!fechaInicioString.equalsIgnoreCase(" ")){
                    Date fechaInicio = dateFormat.parse(fechaInicioString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(fechaInicio);
                    // Restar un día
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    // Obtener la fecha resultante
                    fechaInicio = calendar.getTime();


                    String fechaPartidoString = p.getFechaInicio();
                    Date fechaPartido= dateFormat2.parse(fechaPartidoString);

                    if (!fechaPartido.after(fechaInicio)) {
                        eliminar.add(p);
                    }
                }else if(!fechaFinString.equalsIgnoreCase(" ")){
                    Calendar calendar = Calendar.getInstance();

                    Date fechaFin = dateFormat.parse(fechaFinString);
                    calendar.setTime(fechaFin);
                    // Restar un día
                    calendar.add(Calendar.DAY_OF_YEAR, +1);
                    // Obtener la fecha resultante
                    fechaFin = calendar.getTime();

                    String fechaPartidoString = p.getFechaInicio();
                    Date fechaPartido= dateFormat2.parse(fechaPartidoString);

                    if (!fechaPartido.before(fechaFin)) {
                        eliminar.add(p);
                    }
                }
            }
            ligas.removeAll(eliminar);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        tusLigas.getAdapter().notifyDataSetChanged();

        if(mapa.getVisibility()==View.VISIBLE){
            rellenarMapa();
        }
    }

    private void iniciarDialogo(){
        FiltrarDialogo dialogo = new FiltrarDialogo(this);
        dialogo.show(getActivity().getSupportFragmentManager(), "Filtrar datos");
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
            String[] mensajeRespuesta = respuesta.split(";");
            if (mensajeRespuesta[0].equalsIgnoreCase(Mensajes.LIGUES_AVAILABLE_OK)) {
                ligas.clear();
                for (int i = 1; i < mensajeRespuesta.length; i++) {
                    String[] partesTorneo = mensajeRespuesta[i].split("!");

                    String[] ubicacion = partesTorneo[2].split(",");
                    Double latitud = Double.parseDouble(ubicacion[0].replaceAll(" ",""));
                    Double longitud = Double.parseDouble(ubicacion[1].replaceAll(" ",""));



                    Liga l = new Liga(Integer.parseInt(partesTorneo[0]), partesTorneo[1],latitud, longitud, Double.parseDouble(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                            Integer.parseInt(partesTorneo[5]), partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9], partesTorneo[10],
                            Integer.parseInt(partesTorneo[11]), Integer.parseInt(partesTorneo[12]), partesTorneo[13], partesTorneo[14],
                            partesTorneo[15], Integer.parseInt(partesTorneo[16]));

                    l.setUbicacion(new Direccion(getContext(),latitud,longitud));

                    ligas.add(l);
                    Log.i("LIGA "+i,ligas.get(ligas.size()-1).toString());
                }

                LigaAdapter ligaAdapter = new LigaAdapter(ligas, getContext(), new LigaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Liga item) {
                        moveToDescription(item);
                    }
                });
                tusLigas.setHasFixedSize(true);
                tusLigas.setLayoutManager(new LinearLayoutManager(getContext()));
                tusLigas.setAdapter(ligaAdapter);

                if(mapa.getVisibility()==View.VISIBLE){
                    rellenarMapa();
                }

                ((HomeActivity)getActivity()).completado();
            } else {
                Toast.makeText(getActivity(), "No se pueden obtener las ligas", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }
        }
    }

    public void moveToDescription(Liga t) {
        InformacionLigaFragment nuevoFragmento = new InformacionLigaFragment(t);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, nuevoFragmento);
        transaction.addToBackStack(null);
        transaction.commit();
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