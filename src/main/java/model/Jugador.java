package model;

import java.util.List;

public class Jugador {

    private int id;
    private Jugada mejorJugada;    
    private double puntos;  //Puntos para calcular equity
    private List<Carta> manoInicial; 
        

    public Jugador(List<Carta> c, int id) {
        this.manoInicial = new SortedArrayList<>();
        this.manoInicial.addAll(c);
        this.id = id;
        this.puntos = 0;
    }

    public List<Carta> getCartas() {
        return this.manoInicial;
    }

    public double getPuntos() {
        return this.puntos;
    }
    
    public Jugada getJugada(){
        return this.mejorJugada;
    }

    public tJugada getTipoJugada() {
        return this.mejorJugada.getJugada();
    }
    
    public void sumaPuntos(double d){
        this.puntos += d;
    }

    public void setPuntos(double d) {
        this.puntos = d;
    }

    public void setMejorJugada(Jugada j) {
        this.mejorJugada = j;
    }
}
