package com.example.pi_movil.tarjetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pi_movil.R;
import com.example.pi_movil.datos.Jornada;

import java.util.List;

public class JornadaAdapter extends RecyclerView.Adapter<JornadaAdapter.ViewHolder> {

    private List<Jornada> Jornadas;
    private LayoutInflater mInflater;
    private Context context;

    final JornadaAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Jornada item);
    }

    public JornadaAdapter(List<Jornada> Jornadas, Context context, JornadaAdapter.OnItemClickListener listener) {
        this.mInflater=LayoutInflater.from(context);
        this.Jornadas = Jornadas;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JornadaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.jornada_card,null);
        return new JornadaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JornadaAdapter.ViewHolder holder, int position) {
        holder.bindData(Jornadas.get(position));
    }

    @Override
    public int getItemCount() {
        return Jornadas.size();
    }

    public void setItems(List<Jornada> items){
        Jornadas = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eLocal, ptosLocal, eVisitante, ptosVisitante, fecha, hora;
        ViewHolder(View itemView){
            super(itemView);
            eLocal = itemView.findViewById(R.id.equipoLocalJornada);
            ptosLocal = itemView.findViewById(R.id.puntuacionLocalJornada);
            eVisitante = itemView.findViewById(R.id.equipoVisitanteJornada);
            ptosVisitante = itemView.findViewById(R.id.puntuacionVisitanteJornada);
            fecha = itemView.findViewById(R.id.fechaDeLaJornadaJornada);
            hora = itemView.findViewById(R.id.horaDeLaJornadaJornada);
        }

        void bindData(final Jornada e){
            eLocal.setText(e.getEquipoLocal());
            ptosLocal.setText(e.getPtosLocal());
            eVisitante.setText(e.getEquipoVisitante().toString());
            ptosVisitante.setText(e.getPtosVisitante());
            ptosLocal.setText(e.getPtosLocal());
            fecha.setText(e.getFecha());
            hora.setText(e.getHora());

        }
    }
}


