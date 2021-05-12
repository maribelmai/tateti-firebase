package ar.com.develup.tateti.actividades

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ar.com.develup.tateti.R
import ar.com.develup.tateti.adaptadores.AdaptadorPartidas
import ar.com.develup.tateti.modelo.Constantes
import ar.com.develup.tateti.modelo.Partida
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.actividad_partidas.*

class ActividadPartidas : AppCompatActivity() {

    private lateinit var adaptadorPartidas: AdaptadorPartidas

    private val listenerTablaPartidas: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildAdded: $dataSnapshot")
            val partida = dataSnapshot.getValue(Partida::class.java)!!
            partida.id = dataSnapshot.key
            adaptadorPartidas.agregarPartida(partida)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildChanged: $s")
            val partida = dataSnapshot.getValue(Partida::class.java)!!
            partida.id = dataSnapshot.key
            adaptadorPartidas.partidaCambio(partida)
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.i(TAG, "onChildRemoved: ")
            val partida = dataSnapshot.getValue(Partida::class.java)!!
            partida.id = dataSnapshot.key
            adaptadorPartidas.remover(partida)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
            Log.i(TAG, "onChildMoved: $s")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.i(TAG, "onCancelled: ")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividad_partidas)
        adaptadorPartidas = AdaptadorPartidas(this)
        partidas.layoutManager = LinearLayoutManager(this)
        partidas.adapter = adaptadorPartidas
        nuevaPartida.setOnClickListener { nuevaPartida() }
    }

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance().reference.child(Constantes.TABLA_PARTIDAS).addChildEventListener(listenerTablaPartidas)
    }

    fun nuevaPartida() {
        val intent = Intent(this, ActividadPartida::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = ActividadPartidas::class.java.simpleName
    }
}