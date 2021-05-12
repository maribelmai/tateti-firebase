package ar.com.develup.tateti.modelo

import java.io.Serializable
import java.util.*

/**
 * Created by maribelmai on 26/3/17.
 */
data class Partida(@kotlin.jvm.JvmField var id: String? = null) : Serializable {
    var retador: String? = null
    var oponente: String? = null
    var movimientos: MutableList<Movimiento> = ArrayList()
    var turno: String? = null
    var ganador: String? = null

    fun calcularEstado() = when {
        oponente == null -> "ESPERANDO OPONENTE"
        ganador == null -> "EN JUEGO"
        else -> "PARTIDA FINALIZADA"
    }
}