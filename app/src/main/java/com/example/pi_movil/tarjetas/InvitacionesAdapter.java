package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.EquiposFragment;
import com.example.pi_movil.R;
import com.example.pi_movil.comunicacion.AsyncTasks;
import com.example.pi_movil.comunicacion.Mensajes;
import com.example.pi_movil.comunicacion.Session;
import com.example.pi_movil.datos.Invitacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class InvitacionesAdapter extends RecyclerView.Adapter<InvitacionesAdapter.ViewHolder> {

    private List<Invitacion> invitaciones;
    private LayoutInflater mInflater;
    private Context context;

    final InvitacionesAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Invitacion item);
    }

    public InvitacionesAdapter(List<Invitacion> invitaciones, Context context, InvitacionesAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.invitaciones = invitaciones;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InvitacionesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.invitacion_card,null);
        return new InvitacionesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitacionesAdapter.ViewHolder holder, int position) {
        holder.bindData(invitaciones.get(position));
    }

    @Override
    public int getItemCount() {
        return invitaciones.size();
    }

    public void setItems(List<Invitacion> items){
        invitaciones = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textoInvitacion, remitente;
        ImageView aceptar, eliminar;

        ViewHolder(View itemView){
            super(itemView);
            textoInvitacion = itemView.findViewById(R.id.textoInvitacion);
            remitente = itemView.findViewById(R.id.invitacionNombreRemitente);
            aceptar = itemView.findViewById(R.id.aceptarInitacion);
            eliminar = itemView.findViewById(R.id.borrarInvitacaion);
        }

        void bindData(final Invitacion e){

            remitente.setText(e.getRemitente());

            if (e.getTipo().equalsIgnoreCase("equipo")){
                textoInvitacion.setText("Deseas unirte a:");
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InvitacionesAdapter.Tasks tasks = new InvitacionesAdapter.Tasks();
                        tasks.execute(Mensajes.USER_ANSWER_INVITE+";"+ e.getId()+";"+"SI");
                        invitaciones.remove(e);
                        notifyDataSetChanged();
                    }
                });
                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InvitacionesAdapter.Tasks tasks = new InvitacionesAdapter.Tasks();
                        tasks.execute(Mensajes.USER_ANSWER_INVITE+";"+ e.getId()+";"+"NO");
                        invitaciones.remove(e);
                        notifyDataSetChanged();
                    }
                });


            }else if(e.getTipo().equalsIgnoreCase("usuario")){
                textoInvitacion.setText("Permites la unión de:");
                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InvitacionesAdapter.Tasks tasks = new InvitacionesAdapter.Tasks();
                        tasks.execute(Mensajes.TEAM_ANSWER_INVITE+";"+ e.getId()+";"+"SI");
                        invitaciones.remove(e);
                        notifyDataSetChanged();
                    }
                });
                eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InvitacionesAdapter.Tasks tasks = new InvitacionesAdapter.Tasks();
                        tasks.execute(Mensajes.TEAM_ANSWER_INVITE+";"+ e.getId()+";"+"NO");
                        invitaciones.remove(e);
                        notifyDataSetChanged();
                    }
                });
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(e);
                }
            });
        }
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

            } catch (IOException e) {
            }

            return recibido;
        }

        @Override
        public void onPostExecute(String respuesta) {

            Log.v("RESPUESTA", respuesta);
        }
    }
}
