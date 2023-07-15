package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.datos.Equipo;
import com.example.pi_movil.datos.Partido;

import java.util.List;

public class PartidoAdapter extends RecyclerView.Adapter<PartidoAdapter.ViewHolder> {

    private List<Partido> partidos;
    private LayoutInflater mInflater;
    private Context context;

    final PartidoAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Partido item);
    }

    public PartidoAdapter(List<Partido> partidos, Context context, PartidoAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.partidos = partidos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.partido_card,null);
        return new PartidoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartidoAdapter.ViewHolder holder, int position) {
        holder.bindData(partidos.get(position));
    }

    @Override
    public int getItemCount() {
        return partidos.size();
    }

    public void setItems(List<Partido> items){
        partidos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fecha, deporte, ubicacion;
        ViewHolder(View itemView){
            super(itemView);
            fecha = itemView.findViewById(R.id.fechapartido);
            deporte = itemView.findViewById(R.id.deporteDelpartido);
            ubicacion = itemView.findViewById(R.id.ubicacionDelpartido);
        }

        void bindData(final Partido e){
            fecha.setText(e.getFechaInicio());
            deporte.setText(e.getDeporte());
            ubicacion.setText(e.getUbicacion().toString());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(e);
                }
            });
        }
    }
}