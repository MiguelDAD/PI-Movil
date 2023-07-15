package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.datos.Equipo;

import java.util.List;

public class EquiposAdapter extends RecyclerView.Adapter<EquiposAdapter.ViewHolder> {

    private List<Equipo> equipos;
    private LayoutInflater mInflater;
    private Context context;

    final EquiposAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Equipo item);
    }

    public EquiposAdapter(List<Equipo> equipos, Context context, EquiposAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.equipos = equipos;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EquiposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.equipo_card,null);
        return new EquiposAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquiposAdapter.ViewHolder holder, int position) {
        holder.bindData(equipos.get(position));
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public void setItems(List<Equipo> items){
        equipos = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombreEquipo, deporte, lider, ubicacion;
        LinearLayout ubic;
        ViewHolder(View itemView){
            super(itemView);
            nombreEquipo = itemView.findViewById(R.id.nombreEquipo);
            deporte = itemView.findViewById(R.id.deporteDelEquipo);
            lider = itemView.findViewById(R.id.liderDelEquipo);
            ubic = itemView.findViewById(R.id.linearLayout4EquipoCard);
            ubicacion = itemView.findViewById(R.id.ubicacionDelEquipo);
        }

        void bindData(final Equipo e){
            nombreEquipo.setText(e.getNombreEquipo());
            deporte.setText(e.getDeporte());
            lider.setText(e.getLider());

            if(e.getDireccion() != null){
                ubic.setVisibility(View.VISIBLE);
                ubicacion.setText(e.getDireccion().toString());
            }else{
                ubic.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(e);
                }
            });
        }
    }
}
