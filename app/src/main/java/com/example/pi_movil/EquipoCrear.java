package com.example.pi_movil;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.geocode.Direccion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class EquipoCrear extends Fragment {

    private final static int PLACE_PICKER_REQUEST = 999;

    EditText nombreEquipo;
    Spinner deportes;
    Button crearEquipo;
    TextView ubicacion;
    Direccion direccion;
    Spinner privacidad;

    boolean modificar = false;
    Equipo e;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_equipo_crear, container, false);

        nombreEquipo = root.findViewById(R.id.textoNombreEquipo);
        deportes = root.findViewById(R.id.spinnerDeportes);
        ubicacion = root.findViewById(R.id.datoEquipoUbicacion);
        privacidad = root.findViewById(R.id.spinnerPrivacidad);

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new LocationPickerActivity.Builder()
                        .withGooglePlacesApiKey("AIzaSyDszvn-7Y3ucW7nxCjF6ueSwZdjsj3vzj0")
                        .build(getContext());


                startActivityForResult(intent, PLACE_PICKER_REQUEST);

            }
        });

        insertarDeportes();
        insertarPrivacidades();

        crearEquipo = root.findViewById(R.id.createTeamButton);

        if(modificar){
            nombreEquipo.setText(e.getNombreEquipo());
            nombreEquipo.setEnabled(false);
            nombreEquipo.setTextColor(Color.GRAY);
            deportes.setEnabled(false);
            direccion = e.getDireccion();
            ubicacion.setText(direccion.toString());

            crearEquipo.setText("Modificar");
            crearEquipo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EquipoCrear.Tasks nTasks = new EquipoCrear.Tasks();
                    String privacidadSelec = privacidad.getItemAtPosition(privacidad.getSelectedItemPosition()).toString();
                    nTasks.execute(Mensajes.TEAM_MODIFY+";"+nombreEquipo.getText().toString()+";"+direccion.getLatitud()+","+direccion.getLongitud()+";"+privacidadSelec);
                }
            });
        }else{
            crearEquipo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    crearTeam();
                }
            });
        }

        return root;
    }

    public void modificarEquipo(Equipo e){
        this.e = e;
        modificar = true;
    }

    public void cargarBotonEquipoCrear(){
        modificar = false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Log.d("RESULT****", "OK");
            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            Log.d("LATITUDE****", latitude+"");
            double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
            Log.d("LONGITUDE****", longitude+"");

            direccion = new Direccion(getContext(),latitude,longitude);

            ubicacion.setText(direccion.toString());

        } else {
            Log.d("RESULT****", "CANCELLED");
        }

    }

    private void insertarDeportes(){

        EquipoCrear.Tasks nTasks = new EquipoCrear.Tasks();
        nTasks.execute(Mensajes.GET_SPORT);


    }

    private void insertarPrivacidades(){

        EquipoCrear.Tasks nTasks = new EquipoCrear.Tasks();
        nTasks.execute(Mensajes.TEAMS_PRIVACITY);


    }

    public void crearTeam() {

        if (nombreEquipo.getText().toString().isEmpty() || nombreEquipo.getText().toString().contains(" ")) {
            Toast.makeText(getActivity(), "Nombre de equipo vacio o con espacios", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ubicacion.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Ubicacion incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }


        EquipoCrear.Tasks nTasks = new EquipoCrear.Tasks();
        String deporteSeleccionado = deportes.getItemAtPosition(deportes.getSelectedItemPosition()).toString();
        String privacidadSelec = privacidad.getItemAtPosition(privacidad.getSelectedItemPosition()).toString();
        nTasks.execute(Mensajes.TEAM_CREATE+";"+nombreEquipo.getText().toString()+";"+deporteSeleccionado+";"+direccion.getLatitud()+","+direccion.getLongitud()+";"+privacidadSelec);

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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.SEND_SPORT)) {

                List<String> listaDeportes = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesDeporte = partesRespuesta[i].split(":");

                    listaDeportes.add(partesDeporte[0]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listaDeportes);
                deportes.setAdapter(adapter);
                deportes.setSelection(0);

                if(modificar){
                    for (int i = 0; i < adapter.getCount(); i++) {
                        String elemento =  adapter.getItem(i);
                        if(elemento.equalsIgnoreCase(e.getDeporte())) {
                            deportes.setSelection(i);
                            i = adapter.getCount();
                        }
                    }
                }
                ((HomeActivity)getActivity()).completado();

            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.ERROR_SEND_SPORT)){

                Toast.makeText(getActivity(), "Ha ocurrido un problema con el servidor", Toast.LENGTH_SHORT).show();

                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();

            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_CREATE_OK)){
                Toast.makeText(getActivity(), "Equipo creado correctamente", Toast.LENGTH_SHORT).show();

                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();
            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_CREATE_ERROR)) {
                Toast.makeText(getActivity(), "Ese nombre de equipo ya existe", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else  if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAMS_PRIVACITY_OK)) {

                List<String> listaPrivacidades = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {

                    listaPrivacidades.add(partesRespuesta[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, listaPrivacidades);
                privacidad.setAdapter(adapter);
                privacidad.setSelection(0);

                if(modificar){
                    for (int i = 0; i < adapter.getCount(); i++) {
                        String elemento = adapter.getItem(i);
                        if(elemento.equalsIgnoreCase(e.getPrivacidad())) {
                            privacidad.setSelection(i);
                            i = adapter.getCount();
                        }
                    }
                }
                ((HomeActivity)getActivity()).completado();


            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAMS_PRIVACITY_ERROR)){

                Toast.makeText(getActivity(), "Ha ocurrido un problema con el servidor", Toast.LENGTH_SHORT).show();

                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();

            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_MODIFY_OK)){
                Toast.makeText(getActivity(), "Equipo modificado correctamente", Toast.LENGTH_SHORT).show();

                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();
            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_MODIFY_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo modificar el equipo", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else{
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();
            }
        }
    }


}