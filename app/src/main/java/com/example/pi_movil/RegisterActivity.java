package com.example.pi_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText user, email, paswd;

    TextView loguearse;

    String nombreUsuario;
    String idUsuario;

    Dialog cargandoD;

    public void cargando(){

        cargandoD.setContentView(R.layout.dialog_loading);
        cargandoD.setCancelable(false); // Evita que el diálogo se cierre al tocar fuera de él
        cargandoD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        cargandoD.show();

    }

    public void completado(){
        cargandoD.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cargandoD =  new Dialog(this);

        setContentView(R.layout.activity_register);

        user = findViewById(R.id.usernameRegister);
        email = findViewById(R.id.emailRegister);
        paswd = findViewById(R.id.passwordRegister);

        loguearse = findViewById(R.id.signupText);
        loguearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarLoginActivity();
            }
        });

    }

    public void cargarLoginActivity(){
        Intent siguiente = new Intent(RegisterActivity.this, MainActivity.class);

        startActivity(siguiente);
    }

    public void registarseAcion(View view) {

        String usuario = user.getText().toString();
        String correo = email.getText().toString();
        String contra = paswd.getText().toString();

        if (usuario.isEmpty() || usuario.contains(" ")) {
            Toast.makeText(this, "Nombre de usuario incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (correo.isEmpty() || correo.contains(" ") || !comprobarCorreo(correo)) {
            Toast.makeText(this, "Correo incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contra.isEmpty() || contra.contains(" ")) {
            Toast.makeText(this, "Contraseña incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        String peticion = Mensajes.REGISTER+ ";" + usuario + ";" + correo + ";" + contra+";USUARIO";

        RegisterActivity.Tasks nTasks = new RegisterActivity.Tasks();
        nTasks.execute(peticion);
    }

    private boolean comprobarCorreo(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    class Tasks extends AsyncTasks<String, Float, String> {
        Socket s;
        PrintWriter out = null;
        BufferedReader in = null;

        @Override
        public void onPreExecute() {
            s = Session.getSocket();
            cargando();
        }

        @Override
        public String doInBackground(String... strings) {
            System.out.println("Enviar: " + strings[0]);
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

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "El servidor esta offline", Toast.LENGTH_SHORT).show();
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA", respuesta);
            String[] partesRespuesta = respuesta.split(";");
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.REGISTER_ACCEPT_USER)) {
                idUsuario = partesRespuesta[1];
                nombreUsuario = partesRespuesta[2];

                Intent siguiente = new Intent(RegisterActivity.this, HomeActivity.class);
                Bundle datosEnviar = new Bundle();
                datosEnviar.putString("usuario", nombreUsuario);
                datosEnviar.putString("id", idUsuario);
                siguiente.putExtras(datosEnviar);
                completado();
                startActivity(siguiente);

            } else {
                completado();
                Toast.makeText(getApplicationContext(), "Usuario o e-mail ya registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }



}