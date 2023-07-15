package com.example.pi_movil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Session;

import java.io.IOException;

public class IniciarConexion extends AppCompatActivity {

    EditText ip;
    EditText puerto;
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

        setContentView(R.layout.activity_iniciar_conexion);

        ip = findViewById(R.id.ip);
        puerto = findViewById(R.id.puerto);

        solicitarPermisos();

        cargarPreferencas();
    }

    private void cargarPreferencas(){
        SharedPreferences preferences = getSharedPreferences("conexion", Context.MODE_PRIVATE);
        String ipT = preferences.getString("ip","");
        String puertoE = preferences.getString("puerto","");

        ip.setText(ipT);
        puerto.setText(puertoE);
        // Verificar si hay datos de inicio de sesión guardados
        if (!ipT.isEmpty() && !puertoE.isEmpty()) {
            iniciarConexion();
        } else {
            ip.setText("192.168.43.127");
            puerto.setText("7777");
        }

    }

    private void guardarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("conexion", Context.MODE_PRIVATE);

        String ipT = ip.getText().toString();
        int puertoN;
        try{
            puertoN = Integer.parseInt(puerto.getText().toString());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Puerto incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ip",ipT);
        editor.putString("puerto",""+puertoN);

        editor.commit();
        iniciarConexion();
    }

    private void iniciarConexion(){
        String ipT = ip.getText().toString();
        int puertoN;
        try{
            puertoN = Integer.parseInt(puerto.getText().toString());
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Puerto incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ipT.equals("")){
            Toast.makeText(getApplicationContext(), "Ip incorrecta", Toast.LENGTH_SHORT).show();
            return;
        }else{

            Session.setIp(ipT);
            Session.setPort(puertoN);

            Tasks tarea = new Tasks();
            tarea.execute("Iniciar conexion");
        }
    }

    public void conectarse(View view) {
        guardarPreferencias();
    }

    private void solicitarPermisos(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        }else{
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
    }




    class Tasks extends AsyncTasks<String, Float, String> {
        @Override
        public void onPreExecute() {
            cargando();
        }

        @Override
        public String doInBackground(String... strings) {
            System.out.println(strings[0]);
            try{
                new Session();
                Log.i("Session","Sesion creada");
                return "CONECTADO";
            }catch (IOException e){
                Log.i("Session","Sesion no se puede crear");
                return "ERROR";
            }


        }

        @Override
        public void onPostExecute(String s) {

            if(s.equalsIgnoreCase("CONECTADO")){
                Intent intent = new Intent(IniciarConexion.this, MainActivity.class);
                completado();
                startActivity(intent);
            } else if (s.equalsIgnoreCase("ERROR")) {
                completado();
                Toast.makeText(getApplicationContext(), "NO SE PUEDE CONECTAR CON EL SERVIDOR", Toast.LENGTH_SHORT).show();
                
            }


        }
    }

}