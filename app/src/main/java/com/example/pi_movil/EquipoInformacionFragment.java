package com.example.pi_movil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.dialogos.InvitarUsuarioDialogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EquipoInformacionFragment extends Fragment implements InvitarUsuarioDialogo.InvitarUsuarioDialogoListener {

    private Equipo equipo;
    TextView nombre;
    TextView lider;
    TextView deporte;
    TextView ubicacion;
    TextView privacidad;
    ListView integrantes;
    Button botonOpciones;

    ImageView editar, invitaciones, eliminar;

    boolean unido;

    private InvitarUsuarioDialogo.InvitarUsuarioDialogoListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_equipo_informacion, container, false);

        botonOpciones = root.findViewById(R.id.aniadirUsuarioEquipo);


        nombre = root.findViewById(R.id.equipoInformacionNombre);
        lider = root.findViewById(R.id.equipoInformacionLider);
        deporte = root.findViewById(R.id.equipoInformacionDeporte);
        integrantes = root.findViewById(R.id.listaInformacionEquiposIntegrantes);
        ubicacion  = root.findViewById(R.id.equipoInformacionUbicacion);
        privacidad = root.findViewById(R.id.equipoInformacionPrivacidad);
        editar = root.findViewById(R.id.editarEquipo);
        invitaciones = root.findViewById(R.id.invitacionesEquipo);
        eliminar = root.findViewById(R.id.borrarEquipo);

        if (unido){
            mostrarEquipo();
        }else{
            mostrarEquipoNoInscrito();
        }

        return root;
    }

    public void cargarEquipo(Equipo equipo) {
        this.equipo = equipo;
        unido = true;
    }
    public void cargarEquipoNoInscrito(Equipo equipo){
        this.equipo = equipo;
        unido = false;
    }

    private void mostrarEquipoNoInscrito() {
        Log.i("CARGAR", "CARGANDO EQUIPO");
        nombre.setText(equipo.getNombreEquipo());

        String nLider = equipo.getLider();

        lider.setText(nLider);
        deporte.setText(equipo.getDeporte());
        ubicacion.setText(equipo.getDireccion().toString());
        privacidad.setText(equipo.getPrivacidad());

        //ADD STRING EN EL CONSTRUCTOR PARA VER SI ASÍ EVITO ALERTA QUE SE EJECUTA CUANDO INSTALO LA APK.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, equipo.getIntegrantes());
        integrantes.setAdapter(adapter);

        HomeActivity activity = (HomeActivity) getActivity();

        String usuario = activity.getNombreUsuario();

        for (String miembros : equipo.getIntegrantes()){
            if(miembros.equalsIgnoreCase(usuario)){
                botonOpciones.setText("Ya inscrito");
                botonOpciones.setVisibility(View.VISIBLE);
                botonOpciones.setEnabled(false);
                editar.setVisibility(View.GONE);
                invitaciones.setVisibility(View.GONE);
                eliminar.setVisibility(View.GONE);
                return;
            }
        }

        if (privacidad.getText().toString().equalsIgnoreCase("Publico")) {
            botonOpciones.setText("Unirse");
            botonOpciones.setVisibility(View.VISIBLE);
            editar.setVisibility(View.GONE);
            invitaciones.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
            botonOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unirseEquipo(usuario);
                }
            });
        } else  if (privacidad.getText().toString().equalsIgnoreCase("Con Invitacion")) {
            EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
            nTasks.execute(Mensajes.USER_IS_INVITE + ";" + equipo.getNombreEquipo() + ";" + usuario);
            botonOpciones.setVisibility(View.VISIBLE);
            editar.setVisibility(View.GONE);
            invitaciones.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
            botonOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    solicitarUnion(usuario);
                }
            });
        } else{
            botonOpciones.setText("Privado");
            botonOpciones.setVisibility(View.VISIBLE);
            botonOpciones.setEnabled(false);
            editar.setVisibility(View.GONE);
            invitaciones.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }



    }

    private void unirseEquipo(String usuario){
        EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
        nTasks.execute(Mensajes.JOIN_TEAM + ";" + equipo.getNombreEquipo() + ";" + usuario);
    }
    private void solicitarUnion(String usuario){
        EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
        nTasks.execute(Mensajes.SOLICTE_JOIN_TEAM + ";" + equipo.getNombreEquipo() + ";" + usuario);

    }

    private void mostrarEquipo() {
        Log.i("CARGAR", "CARGANDO EQUIPO");
        nombre.setText(equipo.getNombreEquipo());

        String nLider = equipo.getLider();

        lider.setText(nLider);
        deporte.setText(equipo.getDeporte());
        ubicacion.setText(equipo.getDireccion().toString());
        privacidad.setText(equipo.getPrivacidad());

                                            //ADD STRING EN EL CONSTRUCTOR PARA VER SI ASÍ EVITO ALERTA QUE SE EJECUTA CUANDO INSTALO LA APK.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, equipo.getIntegrantes());
        integrantes.setAdapter(adapter);

        HomeActivity activity = (HomeActivity) getActivity();

        String usuario = activity.getNombreUsuario();

        if (!nLider.equals(usuario)) {
            botonOpciones.setText("Abandonar equipo");
            botonOpciones.setVisibility(View.VISIBLE);
            editar.setVisibility(View.GONE);
            invitaciones.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
            botonOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abandonarEquipo();
                }
            });
        } else {
            botonOpciones.setText("Invitar Usuario");
            botonOpciones.setVisibility(View.VISIBLE);
            editar.setVisibility(View.VISIBLE);
            invitaciones.setVisibility(View.VISIBLE);
            eliminar.setVisibility(View.VISIBLE);
            botonOpciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitarUsu(v);
                }
            });
            invitaciones.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InvitacionesFragment nuevoFragmento = new InvitacionesFragment(Mensajes.TEAM_INVITES+";"+equipo.getNombreEquipo());
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, nuevoFragmento);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eliminarEquipo();
                }
            });

            editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EquipoCrear nuevoFragmento = new EquipoCrear();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, nuevoFragmento);
                    transaction.addToBackStack(null);
                    nuevoFragmento.modificarEquipo(equipo);
                    transaction.commit();
                }
            });
        }

    }

    @Override
    public void applyTexts(String usuario) {
        String usuarioInsertar = usuario;
        Log.i("USUARIO DEVUELTO ", usuario);

        EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
        nTasks.execute(Mensajes.TEAM_INVITE_USER + ";" + equipo.getNombreEquipo() + ";" + usuarioInsertar);

    }

    public void invitarUsu(View view) {
        InvitarUsuarioDialogo dialogo = new InvitarUsuarioDialogo(this);
        dialogo.show(getActivity().getSupportFragmentManager(), "Invitar Usuario");
    }

    private void eliminarEquipo(){
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmacion")
                .setMessage("Deseas eliminar el equipo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
                        nTasks.execute(Mensajes.TEAM_DELETE + ";" + equipo.getNombreEquipo());
                    }})
                .setNegativeButton(android.R.string.no, null).show();



    }


    private void abandonarEquipo(){
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmacion")
                .setMessage("Deseas abandonar el equipo?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        EquipoInformacionFragment.Tasks nTasks = new EquipoInformacionFragment.Tasks();
                        nTasks.execute(Mensajes.TEAM_LEAVE + ";" + equipo.getNombreEquipo());
                    }})
                .setNegativeButton(android.R.string.no, null).show();

       

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
            if (partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_INVITE_USER_OK)) {
                Toast.makeText(getActivity(), "Usuario invitado", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_INVITE_USER_ERROR)){
                Toast.makeText(getActivity(), "Nombre de usuario incorrecto o ya en el equipo", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.JOIN_TEAM_OK)) {
                botonOpciones.setVisibility(View.GONE);
                editar.setVisibility(View.GONE);
                invitaciones.setVisibility(View.GONE);
                eliminar.setVisibility(View.GONE);

                //Obtener la referencia del adaptador del ListView
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) integrantes.getAdapter();

                // Agregar el elemento al adaptador
                adapter.add(partesRespuesta[1]);

                // Notificar al adaptador que se ha agregado un elemento
                adapter.notifyDataSetChanged();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.JOIN_TEAM_ERROR)){
                Toast.makeText(getActivity(), "FALLO EN LA INSERCCIÓN", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.USER_IS_INVITE_OK)) {
                botonOpciones.setText("Ya solicitado");
                botonOpciones.setEnabled(false);
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.USER_IS_INVITE_ERROR)) {
                botonOpciones.setText("Solicitar union");
                botonOpciones.setEnabled(true);
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.SOLICTE_JOIN_TEAM_OK)) {
                botonOpciones.setText("Ya solicitado");
                botonOpciones.setEnabled(false);
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.SOLICTE_JOIN_TEAM_ERROR)){
                Toast.makeText(getActivity(), "FALLO EN LA SOLICITUD", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_LEAVE_OK)){
                Toast.makeText(getActivity(), "Has abandonado", Toast.LENGTH_SHORT).show();
                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_LEAVE_ERROR)){
                Toast.makeText(getActivity(), "No se pudo abandonar", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_DELETE_OK)){
                Toast.makeText(getActivity(), "Equipo eliminado", Toast.LENGTH_SHORT).show();
                EquiposFragment nuevoFragmento = new EquiposFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, nuevoFragmento);
                transaction.addToBackStack(null);
                ((HomeActivity)getActivity()).completado();
                transaction.commit();
            }else if(partesRespuesta[0].equalsIgnoreCase(Mensajes.TEAM_DELETE_ERROR)){
                Toast.makeText(getActivity(), "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                ((HomeActivity)getActivity()).completado();
            }
        }
    }

}