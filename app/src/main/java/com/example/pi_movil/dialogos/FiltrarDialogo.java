package com.example.pi_movil.dialogos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pi_movil.PartidoCrear;
import com.example.pi_movil.PartidosFragment;
import com.example.pi_movil.R;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class FiltrarDialogo extends AppCompatDialogFragment {

    Spinner deportes;
    TextView fInicio;
    TextView fFin;

    private FiltrarDialogo.FiltradoDatos listener;

    public FiltrarDialogo(FiltradoDatos listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_filtrar, null);

        deportes = view.findViewById(R.id.filtrar_deporte);
        rellenarSpinner();

        fInicio = view.findViewById(R.id.datoFiltrarfInicio);
        fFin = view.findViewById(R.id.datoPFiltrarfFin);

        fInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate fechaActual = LocalDate.now();
                DatePickerDialog d = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        fInicio.setText(year+"/"+String.format("%2s", month+"").replace(' ', '0')
                                +"/"+String.format("%2s", day+"").replace(' ', '0'));

                    }
                },fechaActual.getYear(),fechaActual.getMonthValue(),fechaActual.getDayOfMonth());
                d.show();
            }
        });

        fFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate fechaActual = LocalDate.now();
                DatePickerDialog d = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        fFin.setText(year+"/"+String.format("%2s", month+"").replace(' ', '0')
                                +"/"+String.format("%2s", day+"").replace(' ', '0'));

                    }
                },fechaActual.getYear(),fechaActual.getMonthValue(),fechaActual.getDayOfMonth());
                d.show();
            }
        });

        builder.setView(view)
                .setTitle("Filtrar")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String deporteP;
                        if(deportes.getSelectedItemPosition()<0){
                            deporteP= " ";
                        }else{
                            deporteP = deportes.getItemAtPosition(deportes.getSelectedItemPosition()).toString();
                        }

                        String fecInicio = fInicio.getText().toString();
                        if(fecInicio.isEmpty())
                            fecInicio = " ";

                        String fecFin = fFin.getText().toString();
                        if(fecFin.isEmpty())
                            fecFin = " ";

                        listener.filtrarDatos(deporteP+";"+fecInicio+";"+fecFin);
                    }
                });

        return builder.create();
    }

    private void rellenarSpinner(){
        FiltrarDialogo.Tasks nTasks = new FiltrarDialogo.Tasks();
        nTasks.execute(Mensajes.GET_SPORT);
    }

    public interface FiltradoDatos {
        void filtrarDatos(String usuario);
    }

    class Tasks extends AsyncTasks<String, Float, String> {
        Socket s;
        PrintWriter out = null;
        BufferedReader in = null;

        @Override
        public void onPreExecute() {
            s = Session.getSocket();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listaDeportes);
                deportes.setAdapter(adapter);
                deportes.setSelection(-1);

            }else{
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
