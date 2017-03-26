package ar.com.develup.tateti.actividades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.modelo.Constantes;
import ar.com.develup.tateti.modelo.Movimiento;
import ar.com.develup.tateti.modelo.Partida;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by maribelmai on 26/3/17.
 */

public class ActividadPartida extends ActividadBasica {

    private static final String TAG = ActividadPartida.class.getSimpleName();
    private Partida partida;

    @BindView(R.id.tablero)
    LinearLayout tablero;

    private ValueEventListener partidaCambio = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            GenericTypeIndicator<Partida> typeIndicator = new GenericTypeIndicator<Partida>() {};
            Partida partida = dataSnapshot.getValue(typeIndicator);
            partida.setId(dataSnapshot.getKey());
            ActividadPartida.this.partida = partida;
            cargarVistasPartidaIniciada();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(Constantes.EXTRA_PARTIDA)) {
            partida = (Partida) getIntent().getSerializableExtra(Constantes.EXTRA_PARTIDA);
            configurarListeners();
            cargarVistasPartidaIniciada();
        }
    }

    private void configurarListeners() {

        FirebaseDatabase.getInstance().getReference(Constantes.TABLA_PARTIDAS).child(this.partida.getId()).addValueEventListener(this.partidaCambio);
    }

    private void cargarVistasPartidaIniciada() {

        for (Movimiento movimiento : this.partida.getMovimientos()) {

            Button boton = (Button) tablero.findViewWithTag(String.valueOf(movimiento.getPosicion()));

            if (movimiento.getJugador().equals(this.partida.getRetador())) {
                boton.setText("X");
            }
            else {
                boton.setText("O");
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.actividad_partida;
    }

    @OnClick({R.id.posicion1, R.id.posicion2, R.id.posicion3, R.id.posicion4,R.id.posicion5,R.id.posicion6,R.id.posicion7,R.id.posicion8,R.id.posicion9})
    public void jugar(Button button) {

        String posicion = (String) button.getTag();
        Integer numeroPosicion = Integer.valueOf(posicion);

        if (partida == null) {

            button.setText("X");
            crearPartida(numeroPosicion);
        }
        else {

            button.setText("O");
            actualizarPartida(numeroPosicion);
        }
    }

    private void actualizarPartida(Integer posicion) {

        String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.partida.getMovimientos().add(new Movimiento(jugador, posicion));

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenciaPartidas = database.child(Constantes.TABLA_PARTIDAS);
        DatabaseReference referenciaPartida = referenciaPartidas.child(this.partida.getId());
        referenciaPartida.child("movimientos").setValue(this.partida.getMovimientos());
    }

    private void crearPartida(Integer posicion) {

        String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.partida = new Partida();
        this.partida.setRetador(jugador);
        this.partida.getMovimientos().add(new Movimiento(jugador, posicion));

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenciaPartidas = database.child(Constantes.TABLA_PARTIDAS);

        DatabaseReference referenciaPartida = referenciaPartidas.push();
        referenciaPartida.setValue(this.partida);
        this.partida.setId(referenciaPartida.getKey());
        this.configurarListeners();
    }
}
