package com.example.pi_movil;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.datos.Liga;
import com.example.pi_movil.tarjetas.EquiposAdapter;
import com.example.pi_movil.tarjetas.Puntuacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class DatosLigaClasificacionFragment extends Fragment {

    TableLayout table;

    Liga ligaActual;

    public DatosLigaClasificacionFragment(Liga ligaActual) {
        this.ligaActual = ligaActual;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datos_liga_clasificacion, container, false);

        table = view.findViewById(R.id.datosLigaClasificacion);

        return view;
    }

    private void construirCabezera(){

        table.removeAllViews();

        TableRow cabezra = new TableRow(getContext()); //Table row for headers

        TextView tv0 = new TextView(getContext());
        tv0.setText(" Equipo ");
        tv0.setTextSize(24);
        tv0.setTextColor(getResources().getColor(R.color.purple_500));
        tv0.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv0);

        TextView tv1 = new TextView(getContext());
        tv1.setText(" PJ ");
        tv1.setTextSize(24);
        tv1.setTextColor(getResources().getColor(R.color.purple_500));
        tv1.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv1);

        TextView tv2 = new TextView(getContext());
        tv2.setText(" Puntos" );
        tv2.setTextSize(24);
        tv2.setTextColor(getResources().getColor(R.color.purple_500));
        tv2.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv2);

        TextView tv3 = new TextView(getContext());
        tv3.setText(" PA ");
        tv3.setTextSize(24);
        tv3.setTextColor(getResources().getColor(R.color.purple_500));
        tv3.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv3);

        TextView tv4 = new TextView(getContext());
        tv4.setText(" PC ");
        tv4.setTextSize(24);
        tv4.setTextColor(getResources().getColor(R.color.purple_500));
        tv4.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv4);

        TextView tv5 = new TextView(getContext());
        tv5.setText(" DP ");
        tv5.setTextSize(24);
        tv5.setTextColor(getResources().getColor(R.color.purple_500));
        tv5.setTypeface(null, Typeface.BOLD);
        cabezra.addView(tv5);

        table.addView(cabezra);
    }

    private void insertarColumna(String[] datos){
        TableRow fila = new TableRow(getContext()); //Table row for data

        for(int i=0; i<datos.length;i++){
            TextView celda = new TextView(getContext());
            celda.setText(datos[i]);
            celda.setTextColor(Color.BLACK);
            celda.setTextSize(24);
            celda.setGravity(Gravity.CENTER);
            fila.addView(celda);
        }

        table.addView(fila);
    }

    public void rellenarTabla() {
        construirCabezera();
        DatosLigaClasificacionFragment.Tasks nTasks = new DatosLigaClasificacionFragment.Tasks();
        nTasks.execute(Mensajes.LIGUES_SCORE + ";" + ligaActual.getId());

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

            Log.v("RESPUESTA CLASIFICACION", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equals(Mensajes.LIGUES_SCORE_OK)) {
                List<Puntuacion> clasificacion = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] clasificacionPartes = partesRespuesta[i].split(":");
                    Puntuacion p = new Puntuacion(clasificacionPartes[0], Integer.parseInt(clasificacionPartes[1]), Integer.parseInt(clasificacionPartes[2]),
                            Integer.parseInt(clasificacionPartes[3]), Integer.parseInt(clasificacionPartes[4]), Integer.parseInt(clasificacionPartes[5]));
                    clasificacion.add(p);
                }

                Collections.sort(clasificacion);

                for(Puntuacion p : clasificacion){
                    insertarColumna(p.getDatos());
                }

            } else {
                Toast.makeText(getActivity(), "No se pudo obtener clasificaciones", Toast.LENGTH_SHORT).show();
            }
            ((HomeActivity)getActivity()).completado();

        }
    }





}