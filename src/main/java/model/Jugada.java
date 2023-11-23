package model;

import java.util.List;

public class Jugada {

    private List<Carta> cartas; //Las cartas que forman la jugada, ordenados.
    private tJugada tipoJugada; //Tipo de jugada, escalera, color, mano alta...

    public Jugada(List<Carta> cartas, tJugada tipoJugada) {
        this.cartas = cartas;
        this.tipoJugada = tipoJugada;
    }

    //Metodos auxiliares
    //Convierte la lista de cartas en un string representativo: "AhAc5h..."
    /*private void setCadCartas() {

        StringBuilder s = new StringBuilder();

        for (Carta c : cartas) {
            s.append(c.getSimb()).append(c.getPalo());
        }

        this.cadCartas = s.toString();
    }*/

    //Getters y setters 
    public tJugada getJugada() {
        return this.tipoJugada;
    }
    
    public List<Carta> getCartas(){
        return cartas;
    }

}