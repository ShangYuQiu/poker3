package model;

import java.util.List;

public class Jugador {

    private List<Carta> cartas;
    private int id;
    private tJugada mejorJugada;
    private double puntos;

    public Jugador(List<Carta> c, int id) {
        this.cartas = c;
        this.id = id;
        puntos = 0;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public double getPuntos() {
        return puntos;
    }

    public tJugada getJugada() {
        return mejorJugada;
    }

    public void setPuntos(double d) {
        this.puntos = d;
    }

    public void setMejorJugada(tJugada t) {
        this.mejorJugada = t;
    }
}
