package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.tarjetas.EquiposAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class DatosTorneoEquiposFragment extends Fragment {

   Torneo t;

   List<Equipo> equipos;

    public DatosTorneoEquiposFragment(Torneo t) {
        this.t = t;

        equipos = new LinkedList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_torneo_equipos, container, false);

        return view;
    }

    public void obtenerEquipos() {
        DatosTorneoEquiposFragment.Tasks nTasks = new DatosTorneoEquiposFragment.Tasks();
        nTasks.execute(Mensajes.TOURNAMENT_TEAMS+";"+t.getId());

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

            Log.v("RESPUESTA EQUIPO", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TOURNAMENT_TEAMS_OK)) {
                equipos.clear();
                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesEquipos = partesRespuesta[i].split(":");
                    String nombreEqupo = partesEquipos[0];
                    String deporte = partesEquipos[2];
                    String lider = partesEquipos[1];

                    Log.i("EQUIPO " + i + ": ", nombreEqupo + " " + lider + " " +deporte);

                    Equipo e = new Equipo(nombreEqupo, deporte, lider);

                    equipos.add(e);
                }

                EquiposAdapter equiposAdapter = new EquiposAdapter(equipos, getContext(), new EquiposAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Equipo item) {
                        //NO HACE NADA SI CLICKA
                    }
                });
                RecyclerView rv = getView().findViewById(R.id.datosTorneosEquiposRv);
                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(equiposAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (equiposAdapter != null && !equipos.isEmpty()) {
                    rv.setAdapter(equiposAdapter);
                }

            } else {
                Toast.makeText(getActivity(), "No se pueden obtener los equipos", Toast.LENGTH_SHORT).show();
            }

            ((HomeActivity)getActivity()).completado();

        }
    }

}