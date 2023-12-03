package model;

import java.util.ArrayList;
import java.util.List;

public class Jugada {

    private List<Carta> cartas; //Las cartas que forman la jugada, ordenados.
    private tJugada tipoJugada; //Tipo de jugada, escalera, color, mano alta...

    public Jugada(List<Carta> cartas, tJugada tipoJugada) {
        this.cartas = new ArrayList<>(cartas);
        this.tipoJugada = tipoJugada;
    }

    //Getters y setters 
    public tJugada getJugada() {
        return this.tipoJugada;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

}
