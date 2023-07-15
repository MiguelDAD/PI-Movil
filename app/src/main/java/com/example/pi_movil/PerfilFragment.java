package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pi_movil.R;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.dialogos.InvitarUsuarioDialogo;
import com.example.pi_movil.dialogos.SolicitarDeporteDialogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerfilFragment extends Fragment {

    EditText correo;
    EditText passwd;
    Button modificarDatos;
    Button insertarDeporte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        correo = view.findViewById(R.id.modificarCorreo);
        passwd = view.findViewById(R.id.modificarPass);
        modificarDatos = view.findViewById(R.id.modificarDatos);
        insertarDeporte = view.findViewById(R.id.solicitarDeporte);

        modificarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarDatos();
            }
        });

        insertarDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolicitarDeporteDialogo dialogo = new SolicitarDeporteDialogo();
                dialogo.show(getActivity().getSupportFragmentManager(), "Solicitar deporte");
            }
        });

        rellenarCorreo();

        return view;
    }

    private void modificarDatos(){
        String email = correo.getText().toString();
        String contra = passwd.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(getActivity(), "El email no puede estar vacio", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!comprobarCorreo(email)){
            Toast.makeText(getActivity(), "El email es incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        String mensaje = Mensajes.CHANGE_DATA + ";" + email + ";" + contra;

        PerfilFragment.Tasks nTasks = new PerfilFragment.Tasks();
        nTasks.execute(mensaje);

    }

    private boolean comprobarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }


    private void rellenarCorreo(){
        PerfilFragment.Tasks nTasks = new PerfilFragment.Tasks();
        nTasks.execute(Mensajes.USER_EMAIL);
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
            if (partesRespuesta[0].equals(Mensajes.USER_EMAIL_OK)) {
                correo.setText(partesRespuesta[1]);
            } else if (partesRespuesta[0].equals(Mensajes.USER_EMAIL_ERROR)) {
                Toast.makeText(getActivity(), "No se pudo obtener tu correo", Toast.LENGTH_SHORT).show();
            } else if (partesRespuesta[0].equals(Mensajes.ERROR)) {
                Toast.makeText(getActivity(), "Ha ocurrido un error inesperado", Toast.LENGTH_SHORT).show();
            }  else if (partesRespuesta.equals(Mensajes.CHANGE_DATA_OK)) {
                Toast.makeText(getActivity(), "Perfil modificado", Toast.LENGTH_SHORT).show();
            } else if (partesRespuesta.equals(Mensajes.CHANGE_DATA_ERROR)) {
                Toast.makeText(getActivity(), "Ese email ya esta en uso", Toast.LENGTH_SHORT).show();
            }

            ((HomeActivity)getActivity()).completado();
        }
    }
}