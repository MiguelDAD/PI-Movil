package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.datos.Partido;
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.tarjetas.EquiposAdapter;
import com.example.pi_movil.tarjetas.PartidoAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class PartidosFragment extends Fragment {

    Button buscarPartido;
    Button crearPartido;
    List<Partido> partidos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_partidos, container, false);

        partidos = new LinkedList<>();
        cargarPartidos();

        crearPartido = view.findViewById(R.id.buttonCrearPartido);
        buscarPartido = view.findViewById(R.id.buttonBuscarPartidos);

        cargarBotones();
        return view;
    }

    private void cargarPartidos() {

        PartidosFragment.Tasks nTasks = new PartidosFragment.Tasks();
        nTasks.execute(Mensajes.GAMES_USER);

    }

    private void cargarBotones(){
        crearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartidoCrear nuevoFragmento = new PartidoCrear();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buscarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PartidosDisponibles nuevoFragmento = new PartidosDisponibles();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.GAMES_USER_OK)) {
                //ubicacion!fInicio!hInicio!fLimite!hLimiite!coste!deporte!estado!maxInscritos!inscritos!id
                partidos.clear();
                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesPartido = partesRespuesta[i].split("!");
                    String[] ubicacion = partesPartido[0].split(",");
                    double latitud = Double.parseDouble(ubicacion[0].replaceAll(" ",""));
                    double longitud = Double.parseDouble(ubicacion[1].replaceAll(" ",""));
                    String fInicio = partesPartido[1];
                    String hInicio = partesPartido[2];
                    String fLimite = partesPartido[3];
                    String hLimite = partesPartido[4];
                    double coste = Double.parseDouble(partesPartido[5]);
                    String deportePart = partesPartido[6];
                    String estado = partesPartido[7];
                    int maxInscritos = Integer.parseInt(partesPartido[8]);
                    int inscritos = Integer.parseInt(partesPartido[9]);
                    int id = Integer.parseInt(partesPartido[10]);

                    Log.i("PARTIDO " + i + ": ID:", ""+id);
                    Partido p = new Partido(latitud,longitud,coste,hInicio,fInicio,hLimite,fLimite,deportePart,estado,inscritos,maxInscritos,id);
                    p.setUbicacion(new Direccion(getContext(),latitud,longitud));
                    partidos.add(p);

                }

                PartidoAdapter partidosAdapter = new PartidoAdapter(partidos, getContext(), new PartidoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Partido item) {
                        moveToDescription(item);
                    }
                });
                RecyclerView rv = getView().findViewById(R.id.tusPartidos);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(partidosAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (partidosAdapter != null && !partidos.isEmpty()) {
                    rv.setAdapter(partidosAdapter);
                }
                ((HomeActivity)getActivity()).completado();

            } else {
                Toast.makeText(getActivity(), "No se pueden obtener los partidos", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            }
        }
    }
    public void moveToDescription(Partido p){
        PartidoDatos nuevoFragmento = new PartidoDatos(p);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, nuevoFragmento);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}