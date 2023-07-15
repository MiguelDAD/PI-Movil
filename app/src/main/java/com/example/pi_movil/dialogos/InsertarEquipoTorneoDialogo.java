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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pi_movil.DatosTorneoFragment;
import com.example.pi_movil.EquiposFragment;
import com.example.pi_movil.R;
import com.example.pi_movil.TorneosFragment;
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

public class InsertarEquipoTorneoDialogo extends AppCompatDialogFragment {

    private Spinner equipos;
    private InsertarEquipoTorneoDialogo.InsertarEquipoTorneoDialogoListener listener;

    private int idT;

    public InsertarEquipoTorneoDialogo(InsertarEquipoTorneoDialogo.InsertarEquipoTorneoDialogoListener listener, int idTorneo) {
        this.listener = listener;
        idT = idTorneo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_inscribirequipotorneo, null);

        equipos = view.findViewById(R.id.dialog_invitarUsuTorneo_usu);

        rellenarSpinner();

        builder.setView(view)
                .setTitle("Insertar equipo")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (equipos.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {
                            String equipo = equipos.getItemAtPosition(equipos.getSelectedItemPosition()).toString();
                            listener.applyTexts(equipo);
                        } else {
                            dismiss();
                        }

                    }
                });

        return builder.create();
    }

    private void rellenarSpinner(){

        InsertarEquipoTorneoDialogo.Tasks nTasks = new InsertarEquipoTorneoDialogo.Tasks();
        nTasks.execute(Mensajes.TOURNAMENTS_TEAMS_INSCRIBE_USER + ";" + idT);

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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TOURNAMENTS_TEAMS_INSCRIBE_USER_OK)) {

                List<String> misEquipos = new LinkedList<>();
                String[] losEquipos;
                if(partesRespuesta.length>1) {
                    losEquipos = partesRespuesta[1].split(":");
                    for(String nEquipo : losEquipos){
                        misEquipos.add(nEquipo);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, misEquipos);
                    equipos.setAdapter(adapter);
                    equipos.setSelection(0);
                }else{
                    Toast.makeText(getActivity(), "No tienes equipos para inscribirte", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    public interface InsertarEquipoTorneoDialogoListener {
        void applyTexts(String usuario);
    }
}