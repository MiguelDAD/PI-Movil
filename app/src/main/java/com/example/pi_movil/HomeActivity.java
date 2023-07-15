package com.example.pi_movil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.ServerHilo;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.databinding.ActivityHomeBinding;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.dialogos.InvitarUsuarioDialogo;
import com.example.pi_movil.tarjetas.EquiposAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String idUsuario;
    String nombreUsuario;
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

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle objetosEnviados = getIntent().getExtras();
        if (objetosEnviados != null) {
            idUsuario = objetosEnviados.getString("id");
            nombreUsuario = objetosEnviados.getString("usuario");
        }

        binding.bottomNavigationView.setBackground(null);

        //CUANDO SE INICIE POR PRIMERA VEZ MUESTRE UN FRAGMENT:
        cambiarFragment(new PartidosFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.equipo:
                    cambiarFragment(new EquiposFragment());
                    break;
                case R.id.partido:
                    cambiarFragment(new PartidosFragment());
                    break;
                case R.id.torneos:
                    cambiarFragment(new TorneosFragment());
                    break;
                case R.id.liga:
                    cambiarFragment(new LigasFragment());
                    break;
                case R.id.perfil:
                    cambiarFragment(new PerfilFragment());
                    break;
            }

            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomeActivity.Tasks nTasks = new HomeActivity.Tasks();
        nTasks.execute(Mensajes.LOGOUT);
    }

    private void cambiarFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.LOGOUT_SEND)) {
                Toast.makeText(getApplicationContext(), "Te has deslogueado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR HACIENDO EL LOGOUT", Toast.LENGTH_SHORT).show();
            }

            completado();
        }

    }
}





