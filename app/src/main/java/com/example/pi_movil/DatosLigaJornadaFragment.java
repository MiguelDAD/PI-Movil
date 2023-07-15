package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.datos.Jornada;
import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.dialogos.InsertarEquipoLigaDialogo;
import com.example.pi_movil.tarjetas.EquiposAdapter;
import com.example.pi_movil.tarjetas.JornadaAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class DatosLigaJornadaFragment extends Fragment {

    Liga t;

    List<String> numeroJornada;
    List<Jornada> jornadas;

    Spinner spinner;
    RecyclerView rv;

    public DatosLigaJornadaFragment(Liga t) {
        this.t = t;
        numeroJornada = new LinkedList<>();
        jornadas = new LinkedList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_datos_liga_jornada, container, false);

        spinner = view.findViewById(R.id.spinnerJornada);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String jornadaEscogida = spinner.getSelectedItem().toString();
                cambiarJornada(jornadaEscogida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }

        });


        rv = view.findViewById(R.id.datosLigaJornadaRv);
        return view;
    }

    public void cargarJornadas(){

        DatosLigaJornadaFragment.Tasks nTasks = new DatosLigaJornadaFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_NUMBER_ROUNDS + ";" + t.getId());

    }

    public void cambiarJornada(String jornada){

        String numeroJornada = jornada.substring(jornada.indexOf(" ") + 1);
        DatosLigaJornadaFragment.Tasks nTasks = new DatosLigaJornadaFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_ROUND_DATE + ";" + t.getId() + ";" + numeroJornada);

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

            Log.v("RESPUESTA JORNADA", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equals(Mensajes.LIGUES_NUMBER_ROUNDS_OK)) {
                numeroJornada.clear();
                for (int i = 1; i <= Integer.parseInt(partesRespuesta[1]); i++) {

                    numeroJornada.add("Jornada " + i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, numeroJornada);
                spinner.setAdapter(adapter);
                spinner.setSelection(0);

            }else if (partesRespuesta[0].equals(Mensajes.LIGUES_ROUND_DATE_OK)) {
                jornadas.clear();
                String[] partesJornada = partesRespuesta[1].split("!");
                for (int i = 0; i < partesJornada.length; i++) {
                    String[] jornada = partesJornada[i].split("/");
                    //id;eLocal;ptosLocal;eVisitante;ptosVisiante;fecha;hora!
                    Jornada j = new Jornada(Integer.parseInt(jornada[0]), jornada[1], jornada[2], jornada[3], jornada[4],
                            jornada[5], jornada[6]);

                    jornadas.add(j);
                }

                JornadaAdapter jornadaAdapter = new JornadaAdapter(jornadas, getContext(), new JornadaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Jornada item) {
                        //
                    }
                });

                rv.setHasFixedSize(true);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(jornadaAdapter);


            } else if (partesRespuesta[0].equals(Mensajes.LIGUES_ROUND_DATE_ERROR)) {
                jornadas.clear();

            } else if (partesRespuesta[0].equals(Mensajes.ERROR)) {
                numeroJornada.clear();
                jornadas.clear();

            }
            ((HomeActivity)getActivity()).completado();

        }
    }

}