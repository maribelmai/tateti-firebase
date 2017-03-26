package ar.com.develup.tateti.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maribelmai on 26/3/17.
 */

public class Partida {

    private String retador;
    private String oponente;
    private List<Movimiento> movimientos = new ArrayList<>();
    private String turno;
    private String ganador;

    public String getRetador() {
        return retador;
    }

    public void setRetador(String retador) {
        this.retador = retador;
    }

    public String getOponente() {
        return oponente;
    }

    public void setOponente(String oponente) {
        this.oponente = oponente;
    }

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getGanador() {
        return ganador;
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }
}
