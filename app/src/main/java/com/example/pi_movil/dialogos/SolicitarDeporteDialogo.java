package com.example.pi_movil.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pi_movil.HomeActivity;
import com.example.pi_movil.R;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class SolicitarDeporteDialogo extends AppCompatDialogFragment {

    private EditText nombre;
    private EditText ctdadTotal;
    private EditText ctdadEquipo;
    private EditText breveDesc;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_aniadirdeporte, null);

        nombre = view.findViewById(R.id.dialog_nombreDto);
        ctdadTotal = view.findViewById(R.id.dialog_ctdad);
        ctdadEquipo = view.findViewById(R.id.dialog_ctdadEq);
        breveDesc = view.findViewById(R.id.dialog_desc);


        builder.setView(view)
                .setTitle("Solicitar deporte")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (nombre.getText().toString().isEmpty()||ctdadEquipo.getText().toString().isEmpty()
                                ||ctdadTotal.getText().toString().isEmpty()||breveDesc.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "Faltan datos por rellenar", Toast.LENGTH_SHORT).show();
                        } else {
                               solicitarDeporte();
                            }
                    }
                });

        return builder.create();
    }

    private void solicitarDeporte(){
        String nombreDto = nombre.getText().toString();
        String ctdadT = ctdadTotal.getText().toString();
        String ctdadE = ctdadEquipo.getText().toString();
        String desc = breveDesc.getText().toString().replaceAll("\n"," ");

        SolicitarDeporteDialogo.Tasks nTasks = new SolicitarDeporteDialogo.Tasks();
        nTasks.execute(Mensajes.REQUEST_SPORT + ";" + nombreDto+";"+ctdadT+";"+ctdadE+";"+desc);

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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.REQUEST_SPORT_OK)) {
                Toast.makeText(getActivity(), "Solicitud enviada", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        }
    }
}
