package ar.com.develup.tateti.adaptadores;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.actividades.ActividadBasica;
import ar.com.develup.tateti.actividades.ActividadPartida;
import ar.com.develup.tateti.modelo.Constantes;
import ar.com.develup.tateti.modelo.Partida;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by maribelmai on 26/3/17.
 */

public class AdaptadorPartidas extends RecyclerView.Adapter<AdaptadorPartidas.Holder> {

    private List<Partida> partidas = new ArrayList<>();
    private ActividadBasica actividad;

    public AdaptadorPartidas(ActividadBasica actividadBasica) {
        this.actividad = actividadBasica;
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
        holder.idPartida.setText(partida.getId());

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

    class Holder extends RecyclerView.ViewHolder {

        Partida partida;

        @BindView(R.id.idPartida)
        TextView idPartida;
        @BindView(R.id.estado)
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
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this.clickPartidaListener);
        }
    }
}
