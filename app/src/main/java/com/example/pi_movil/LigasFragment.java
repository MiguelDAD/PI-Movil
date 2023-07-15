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
import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.datos.Torneo;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.tarjetas.LigaAdapter;
import com.example.pi_movil.tarjetas.TorneoAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class LigasFragment extends Fragment {

    RecyclerView tusLigas;
    Button buscarLiga;
    List<Liga> ligas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ligas, container, false);
        tusLigas = view.findViewById(R.id.tusLigas);
        buscarLiga = view.findViewById(R.id.buttonBuscarLigas);
        ligas=new LinkedList<>();

        obtenerMisLigas();

        buscarLiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LigasDisponiblesFragment nuevoFragmento = new LigasDisponiblesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    public void obtenerMisLigas() {
        LigasFragment.Tasks nTasks = new LigasFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_USER);

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
            if (mensajeRespuesta[0].equals(Mensajes.LIGUES_USER_OK)) {
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
                        Log.i("ENTRANDO", "onItemClick: LIGA");
                        moveToDescription(item);
                    }
                });
                tusLigas.setHasFixedSize(true);
                tusLigas.setLayoutManager(new LinearLayoutManager(getContext()));
                tusLigas.setAdapter(ligaAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (ligaAdapter != null && !ligas.isEmpty()) {
                    tusLigas.setAdapter(ligaAdapter);
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
}