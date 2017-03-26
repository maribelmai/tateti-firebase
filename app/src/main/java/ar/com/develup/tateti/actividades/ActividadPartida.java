package ar.com.develup.tateti.actividades;

import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ar.com.develup.tateti.R;
import ar.com.develup.tateti.modelo.Constantes;
import ar.com.develup.tateti.modelo.Movimiento;
import ar.com.develup.tateti.modelo.Partida;
import butterknife.OnClick;

/**
 * Created by maribelmai on 26/3/17.
 */

public class ActividadPartida extends ActividadBasica {

    private static final String TAG = ActividadPartida.class.getSimpleName();

    private String keyPartida;
    private Partida partida;

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

        }
    }

    private void crearPartida(Integer posicion) {

        String jugador = FirebaseAuth.getInstance().getCurrentUser().getUid();

        partida = new Partida();
        partida.setRetador(jugador);
        partida.getMovimientos().add(new Movimiento(jugador, posicion));

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenciaPartidas = database.child(Constantes.TABLA_PARTIDAS);

        DatabaseReference referenciaPartida = referenciaPartidas.push();
        referenciaPartida.setValue(partida);

        keyPartida = referenciaPartida.getKey();
    }
}
