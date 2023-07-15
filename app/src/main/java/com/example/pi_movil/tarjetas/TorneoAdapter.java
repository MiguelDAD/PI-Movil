package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.datos.Torneo;

import java.util.List;

public class TorneoAdapter extends RecyclerView.Adapter<TorneoAdapter.ViewHolder> {

    private List<Torneo> torneos;
    private LayoutInflater mInflater;
    private Context context;

    final TorneoAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Torneo item);
    }

    public TorneoAdapter(List<Torneo> torneos, Context context, TorneoAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.torneos = torneos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TorneoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.torneo_card,null);
        return new TorneoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TorneoAdapter.ViewHolder holder, int position) {
        holder.bindData(torneos.get(position));
    }

    @Override
    public int getItemCount() {
        return torneos.size();
    }

    public void setItems(List<Torneo> items){
        torneos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreTorneo, deporte, ubicacion;
        ViewHolder(View itemView){
            super(itemView);
            nombreTorneo = itemView.findViewById(R.id.nombreTorneo);
            deporte = itemView.findViewById(R.id.deporteDelTorneo);
            ubicacion = itemView.findViewById(R.id.ubicacionDelTorneo);
        }

        void bindData(final Torneo e){
            nombreTorneo.setText(e.getNombre());
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
