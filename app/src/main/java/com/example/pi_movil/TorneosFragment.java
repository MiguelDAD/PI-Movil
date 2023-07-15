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
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.tarjetas.EquiposAdapter;
import com.example.pi_movil.tarjetas.TorneoAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class TorneosFragment extends Fragment {

    RecyclerView tusTorneos;
    Button buscarTorneo;
    List<Torneo> torneos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_torneos, container, false);

        tusTorneos = view.findViewById(R.id.tusTorneos);
        buscarTorneo = view.findViewById(R.id.buttonBuscarTorneos);
        torneos=new LinkedList<>();

        obtenerMisTorneos();

        buscarTorneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TorneosDisponiblesFragment nuevoFragmento = new TorneosDisponiblesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }


    public void obtenerMisTorneos() {
        TorneosFragment.Tasks nTasks = new TorneosFragment.Tasks();
        nTasks.execute(Mensajes.TOURNAMENTS_USER);

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

                if (recibido != null)
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
            if (mensajeRespuesta[0].equals(Mensajes.TOURNAMENTS_USER_OK)) {
                for (int i = 1; i < mensajeRespuesta.length; i++) {
                    String[] partesTorneo = mensajeRespuesta[i].split("!");

                    String[] ubicacion = partesTorneo[1].split(",");
                    Double latitud = Double.parseDouble(ubicacion[0].replaceAll(" ",""));
                    Double longitud = Double.parseDouble(ubicacion[1].replaceAll(" ",""));

                    Torneo t = new Torneo(partesTorneo[0], latitud,longitud, Double.parseDouble(partesTorneo[2]), Integer.parseInt(partesTorneo[3]), Integer.parseInt(partesTorneo[4]),
                            partesTorneo[5], partesTorneo[6], partesTorneo[7], partesTorneo[8], partesTorneo[9],
                            partesTorneo[10],Integer.parseInt(partesTorneo[11]),Integer.parseInt(partesTorneo[12]));

                    t.setUbicacion(new Direccion(getContext(),latitud,longitud));

                    torneos.add(t);
                    Log.i("TORNEOS "+i,torneos.get(torneos.size()-1).toString());
                }

                TorneoAdapter torneoAdapter = new TorneoAdapter(torneos, getContext(), new TorneoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Torneo item) {
                        moveToDescription(item);
                    }
                });
                tusTorneos.setHasFixedSize(true);
                tusTorneos.setLayoutManager(new LinearLayoutManager(getContext()));
                tusTorneos.setAdapter(torneoAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (torneoAdapter != null && !torneos.isEmpty()) {
                    tusTorneos.setAdapter(torneoAdapter);
                }

                ((HomeActivity)getActivity()).completado();

            } else {
                Toast.makeText(getActivity(), "No se pueden obtener los torneos", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();

            }
        }
    }

    public void moveToDescription(Torneo t){
        InformacionTorneoFragment nuevoFragmento = new InformacionTorneoFragment(t);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, nuevoFragmento);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}