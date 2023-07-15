package com.example.pi_movil;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Invitacion;
import com.example.pi_movil.datos.Partido;
import com.example.pi_movil.geocode.Direccion;
import com.example.pi_movil.tarjetas.InvitacionesAdapter;
import com.example.pi_movil.tarjetas.PartidoAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class InvitacionesFragment extends Fragment {

    RecyclerView solicitudes;
    String mensaje;

    public InvitacionesFragment(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitaciones, container, false);

        solicitudes = view.findViewById(R.id.listaInvitaciones);
        cargarInvitaciones();

        return view;
    }

    private void cargarInvitaciones(){
        InvitacionesFragment.Tasks nTasks = new InvitacionesFragment.Tasks();
        nTasks.execute(mensaje);
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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.USER_INVITES_OK)) {
                List<Invitacion> invitaciones = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesInvti = partesRespuesta[i].split(":");

                    int id = Integer.parseInt(partesInvti[0]);
                    String remitente = partesInvti[1];

                    Log.i("INVITACION " + i + ": ID:", ""+id);
                    Invitacion invi = new Invitacion(id,remitente);
                    invi.setTipo("equipo");
                    invitaciones.add(invi);

                }

                InvitacionesAdapter invitacionesAdapter = new InvitacionesAdapter(invitaciones, getContext(), new InvitacionesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Invitacion item) {
                        //
                    }
                });
                solicitudes.setHasFixedSize(true);
                solicitudes.setLayoutManager(new LinearLayoutManager(getContext()));
                solicitudes.setAdapter(invitacionesAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (invitacionesAdapter != null && !invitaciones.isEmpty()) {
                    solicitudes.setAdapter(invitacionesAdapter);
                }
                ((HomeActivity)getActivity()).completado();

            }else if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_INVITES_OK)) {
                List<Invitacion> invitaciones = new LinkedList<>();

                for (int i = 1; i < partesRespuesta.length; i++) {
                    String[] partesInvti = partesRespuesta[i].split(":");

                    int id = Integer.parseInt(partesInvti[0]);
                    String remitente = partesInvti[1];

                    Log.i("INVITACION " + i + ": ID:", ""+id);
                    Invitacion invi = new Invitacion(id,remitente);
                    invi.setTipo("usuario");
                    invitaciones.add(invi);

                }

                InvitacionesAdapter invitacionesAdapter = new InvitacionesAdapter(invitaciones, getContext(), new InvitacionesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Invitacion item) {
                        //
                    }
                });
                solicitudes.setHasFixedSize(true);
                solicitudes.setLayoutManager(new LinearLayoutManager(getContext()));
                solicitudes.setAdapter(invitacionesAdapter);

                // comprobar si el adaptador y la lista no son nulos o vacíos
                if (invitacionesAdapter != null && !invitaciones.isEmpty()) {
                    solicitudes.setAdapter(invitacionesAdapter);
                }
                ((HomeActivity)getActivity()).completado();
            }
            else {
                Toast.makeText(getActivity(), "No se pueden obtener las invitaciones", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }
        }
    }



}