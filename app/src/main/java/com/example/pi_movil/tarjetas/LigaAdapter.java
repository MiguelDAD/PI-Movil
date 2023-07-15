package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.datos.Liga;

import java.util.List;

public class LigaAdapter extends RecyclerView.Adapter<LigaAdapter.ViewHolder> {

    private List<Liga> ligas;
    private LayoutInflater mInflater;
    private Context context;

    final LigaAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Liga item);
    }

    public LigaAdapter(List<Liga> ligas, Context context, LigaAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.ligas = ligas;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LigaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.liga_card,null);
        return new LigaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LigaAdapter.ViewHolder holder, int position) {
        holder.bindData(ligas.get(position));
    }

    @Override
    public int getItemCount() {
        return ligas.size();
    }

    public void setItems(List<Liga> items){
        ligas = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreLiga, deporte, ubicacion;
        ViewHolder(View itemView){
            super(itemView);
            nombreLiga = itemView.findViewById(R.id.nombreLiga);
            deporte = itemView.findViewById(R.id.deporteDeLaLiga);
            ubicacion = itemView.findViewById(R.id.ubicacionDeLaLiga);
        }

        void bindData(final Liga e){
            nombreLiga.setText(e.getNombre());
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

