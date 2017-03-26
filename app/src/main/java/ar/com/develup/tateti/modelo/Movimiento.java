package ar.com.develup.tateti.modelo;

/**
 * Created by maribelmai on 26/3/17.
 */

public class Movimiento {

    private Integer posicion;
    private String jugador;

    public Movimiento(String jugador, Integer posicion) {
        this.jugador = jugador;
        this.posicion = posicion;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }
}
