package com.example.pi_movil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.ServerHilo;
import com.example.pi_movil.comunicacion.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText usuario;
    EditText passwd;
    Button iniciarSesion;

    TextView registrarse;

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

        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.username);
        passwd = findViewById(R.id.password);
        iniciarSesion = findViewById(R.id.loginButton);

        registrarse = findViewById(R.id.registerText);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarRegisterActivity();
            }
        });



    }

    public void cargarRegisterActivity(){
        Intent siguiente = new Intent(MainActivity.this, RegisterActivity.class);

        startActivity(siguiente);
    }

    public void iniciarSesion(View view) {

        String user = usuario.getText().toString();
        String pass = passwd.getText().toString();

        if (user.isEmpty() || user.contains(" ")) {
            Toast.makeText(this, "Nombre de usuario incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty() || pass.contains(" ")) {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }


        String peticion = Mensajes.LOGIN + ";" + user + ";" + pass + ";USUARIO";



        Tasks nTasks = new Tasks();
        nTasks.execute(peticion);


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

                System.out.println("Recibido del servidor: " + recibido);
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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.LOGIN_ACCEPT_USER)) {
                idUsuario = partesRespuesta[1];
                nombreUsuario = partesRespuesta[2];

                Intent siguiente = new Intent(MainActivity.this, HomeActivity.class);
                Bundle datosEnviar = new Bundle();
                datosEnviar.putString("usuario", nombreUsuario);
                datosEnviar.putString("id", idUsuario);
                siguiente.putExtras(datosEnviar);
                completado();
                startActivity(siguiente);

            } else {
                completado();
                Toast.makeText(getApplicationContext(), "Datos incorrecto del usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

