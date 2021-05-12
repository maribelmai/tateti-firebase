package ar.com.develup.tateti.adaptadores

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.com.develup.tateti.R
import ar.com.develup.tateti.actividades.ActividadPartida
import ar.com.develup.tateti.actividades.ActividadPartidas
import ar.com.develup.tateti.modelo.Constantes
import ar.com.develup.tateti.modelo.Partida
import java.util.*

class AdaptadorPartidas(
        private val actividad: ActividadPartidas
) : RecyclerView.Adapter<AdaptadorPartidas.Holder>() {

    private val partidas: MutableList<Partida> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.item_partida, null)
        return Holder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val partida = partidas[position]
        holder.partida = partida
        holder.idPartida.text = partida.id
        holder.estado.text = partida.calcularEstado()
    }

    override fun getItemCount(): Int {
        return partidas.size
    }

    fun agregarPartida(partida: Partida) {
        if (!partidas.contains(partida)) {
            partidas.add(partida)
            notifyItemInserted(partidas.size - 1)
        }
    }

    fun partidaCambio(partida: Partida) {
        if (partidas.contains(partida)) {
            val posicion = partidas.indexOf(partida)
            partidas[posicion] = partida
            notifyItemChanged(posicion)
        }
    }

    fun remover(partida: Partida?) {
        if (partidas.contains(partida)) {
            val posicion = partidas.indexOf(partida)
            partidas.remove(partida)
            notifyItemRemoved(posicion)
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var partida: Partida? = null
        var idPartida: TextView
        var estado: TextView

        private val clickPartidaListener = View.OnClickListener {
            val intent = Intent(actividad, ActividadPartida::class.java)
            intent.putExtra(Constantes.EXTRA_PARTIDA, partida)
            actividad.startActivity(intent)
        }

        init {
            itemView.setOnClickListener(clickPartidaListener)
            idPartida = itemView.findViewById(R.id.idPartida)
            estado = itemView.findViewById(R.id.estado)
        }
    }

}