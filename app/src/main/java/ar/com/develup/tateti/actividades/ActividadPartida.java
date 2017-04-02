package ar.com.develup.tateti.actividades;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
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

    @BindView(R.id.rootView)
    View rootView;

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

            if (partida.getOponente() == null) {
                sumarmeComoOponente();
            }

            configurarListeners();
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

            boton.setEnabled(false);
        }

        comprobarGanador();
    }

    private void comprobarGanador() {

        if (hayTaTeTi()) {

            String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Partida finalizada");
            builder.setMessage(partida.getGanador().equals(jugador)? "GANASTE!" : "PERDISTE :(");

            try {
                builder.show();
            }
            catch (Exception ignored){}
        }
        else if (finalizo()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Partida finalizada");
            builder.setMessage("Es un empate");

            try {
                builder.show();
            }
            catch (Exception ignored){}
        }
    }

    private boolean finalizo() {
        return this.partida.getMovimientos().size() == 9;
    }

    private boolean hayTaTeTi() {
        
        return sonIguales(1,2,3) || sonIguales(4,5,6) || sonIguales(7,8,9) //Horizontal
                || sonIguales(1,4,7) || sonIguales(2,5,8) || sonIguales(3,6,9) //Vertical
                || sonIguales(1,5,9) || sonIguales(3,5,7); //Diagonal
    }

    private boolean sonIguales(Integer... casilleros) {

        Boolean sonIguales = true;
        String valor = null;

        for (int i = 0; i < casilleros.length && sonIguales; i++) {

            Button boton = (Button) tablero.findViewWithTag(String.valueOf(casilleros[i]));
            String simbolo = boton.getText().toString();

            if (valor == null) {
                valor = simbolo;
            }
            else {
                sonIguales = !simbolo.isEmpty() && valor.equals(simbolo);
            }
        }

        if (sonIguales) {

            for (Integer casillero : casilleros) {
                Button boton = (Button) tablero.findViewWithTag(String.valueOf(casillero));
                boton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }

            if (("X").equalsIgnoreCase(valor)) {
                establecerGanador(partida.getRetador());
            }
            else if (("O").equalsIgnoreCase(valor)) {
                establecerGanador(partida.getOponente());
            }
        }
        return sonIguales;
    }

    private void establecerGanador(String ganador) {

        partida.setGanador(ganador);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenciaPartidas = database.child(Constantes.TABLA_PARTIDAS);
        DatabaseReference referenciaPartida = referenciaPartidas.child(this.partida.getId());
        referenciaPartida.child("ganador").setValue(ganador);
    }

    @Override
    protected int getLayout() {
        return R.layout.actividad_partida;
    }

    @OnClick({R.id.posicion1, R.id.posicion2, R.id.posicion3, R.id.posicion4,R.id.posicion5,R.id.posicion6,R.id.posicion7,R.id.posicion8,R.id.posicion9})
    public void jugar(Button button) {

        if (esMiTurno()) {

            String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String posicion = (String) button.getTag();
            Integer numeroPosicion = Integer.valueOf(posicion);

            if (partida == null) {

                button.setText("X");
                crearPartida(numeroPosicion);
            }
            else if (partida.getGanador() == null) {

                if (partida.getRetador().equals(jugador)) {
                    button.setText("X");
                }
                else if (partida.getOponente().equals(jugador)) {
                    button.setText("O");
                }
                actualizarPartida(numeroPosicion);
            }
        }
        else if (partida.getGanador() == null) {
            Snackbar.make(rootView, "Es el turno de tu oponente", Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean esMiTurno() {

        String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return this.partida == null
                ||
                !this.partida.getMovimientos().get(this.partida.getMovimientos().size() -1).getJugador().equals(jugador);
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

    private void sumarmeComoOponente() {

        String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.partida.setOponente(jugador);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenciaPartidas = database.child(Constantes.TABLA_PARTIDAS);
        DatabaseReference referenciaPartida = referenciaPartidas.child(this.partida.getId());
        referenciaPartida.child("oponente").setValue(jugador);
    }
}
