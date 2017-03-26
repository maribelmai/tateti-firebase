package ar.com.develup.tateti.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maribelmai on 26/3/17.
 */

public class Partida {

    private transient String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partida partida = (Partida) o;

        return id != null ? id.equals(partida.id) : partida.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
