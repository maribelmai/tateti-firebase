package ar.com.develup.tateti.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.adaptadores.AdaptadorPartidas;
import ar.com.develup.tateti.modelo.Constantes;
import ar.com.develup.tateti.modelo.Partida;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by maribelmai on 26/3/17.
 */

public class ActividadPartidas extends ActividadBasica {

    private static final String TAG = ActividadPartidas.class.getSimpleName();
    @BindView(R.id.partidas)
    RecyclerView partidasRecycler;

    private AdaptadorPartidas adaptadorPartidas;
    private ChildEventListener listenerTablaPartidas = new ChildEventListener() {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.i(TAG, "onChildAdded: " + dataSnapshot);

            GenericTypeIndicator<Partida> typeIndicator = new GenericTypeIndicator<Partida>() {};
            Partida partida = dataSnapshot.getValue(typeIndicator);
            partida.setId(dataSnapshot.getKey());
            adaptadorPartidas.agregarPartida(partida);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.i(TAG, "onChildChanged: " + s);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.i(TAG, "onChildRemoved: ");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.i(TAG, "onChildMoved: " + s);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(TAG, "onCancelled: ");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adaptadorPartidas = new AdaptadorPartidas(this);
        this.partidasRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.partidasRecycler.setAdapter(adaptadorPartidas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child(Constantes.TABLA_PARTIDAS).addChildEventListener(this.listenerTablaPartidas);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase.getInstance().getReference().child(Constantes.TABLA_PARTIDAS).removeEventListener(this.listenerTablaPartidas);
    }

    @Override
    protected int getLayout() {
        return R.layout.actividad_partidas;
    }

    @OnClick(R.id.nuevaPartida)
    public void nuevaPartida() {
        Intent intent = new Intent(this, ActividadPartida.class);
        startActivity(intent);
    }
}
