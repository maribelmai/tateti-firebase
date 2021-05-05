package ar.com.develup.tateti.adaptadores;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.actividades.ActividadPartida;
import ar.com.develup.tateti.actividades.ActividadPartidas;
import ar.com.develup.tateti.modelo.Constantes;
import ar.com.develup.tateti.modelo.Partida;

/**
 * Created by maribelmai on 26/3/17.
 */

public class AdaptadorPartidas extends RecyclerView.Adapter<AdaptadorPartidas.Holder> {

    private List<Partida> partidas = new ArrayList<>();
    private ActividadPartidas actividad;

    public AdaptadorPartidas(ActividadPartidas actividad) {
        this.actividad = actividad;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partida, null);
        return new Holder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Partida partida = partidas.get(position);
        holder.partida = partida;
        holder.idPartida.setText(partida.id);

        String estado = "";
        if (partida.getOponente() == null) {
            estado = "ESPERANDO OPONENTE";
        }
        else if (partida.getGanador() == null) {
            estado = "EN JUEGO";
        }
        else {
            estado = "PARTIDA FINALIZADA";
        }

        holder.estado.setText(estado);
    }

    @Override
    public int getItemCount() {
        return partidas.size();
    }

    public void agregarPartida(Partida partida) {

        if (!partidas.contains(partida)) {
            partidas.add(partida);
            notifyItemInserted(partidas.size() - 1);
        }
    }

    public void partidaCambio(Partida partida) {

        if (partidas.contains(partida)) {
            int posicion = partidas.indexOf(partida);
            partidas.set(posicion, partida);
            notifyItemChanged(posicion);
        }
    }

    public void remover(Partida partida) {

        if (partidas.contains(partida)) {
            int posicion = partidas.indexOf(partida);
            partidas.remove(partida);
            notifyItemRemoved(posicion);
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        Partida partida;

        TextView idPartida;
        TextView estado;

        private final View.OnClickListener clickPartidaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(actividad, ActividadPartida.class);
                intent.putExtra(Constantes.EXTRA_PARTIDA, partida);
                actividad.startActivity(intent);
            }
        };

        public Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this.clickPartidaListener);
            this.idPartida = itemView.findViewById(R.id.idPartida);
            this.estado = itemView.findViewById(R.id.estado);
        }
    }
}
