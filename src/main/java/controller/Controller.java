package controller;

import gui.MainFrame;
import java.util.List;
import model.*;
import java.util.Map;

public class Controller {

    private MainFrame frame;
    private Logic logica;

    public Controller(Logic logica, MainFrame frame) {
        this.frame = frame;
        this.logica = logica;

    }

    //aniadir las cartas random del jugador elegido a la lista jug
    public void randomJugCard(int jug) {
        logica.randomJugCard(jug);
    }

    //devuelve cartas del jugador elegido
    public List<Carta> getJugadorCarta(int jug) {
        return logica.getJugadorCarta(jug);
    }

    //aniadir las cartas random del board a la lista board
    public void randomBoardCard() {
        logica.randomBoardCard();
    }

    //devuelve las cartas board
    public List<Carta> getBoardCard() {
        return logica.getBoardCard();
    }

    //aniadir las cartas introducido por el jugador elegido a la lista jug
    public void enterJugCard(int jug, String carta) {
        logica.enterJugCard(jug, carta);
    }

    //aniadir las cartas boards introducido a la lista board
    public void enterBoardCard(String carta) {
        logica.enterBoardCard(carta);
    }

    public void setLogica() {
        this.logica = new Logic();
    }
    public double getEquity(int id){
        if(this.logica.getEquity().get(id) != null){
            return this.logica.getEquity().get(id);
        }
        
        else {
            return 0.0;
        }
    }
    public void calcularPuntosJugadores(int numCartasAleatorias){
        this.logica.calcularPuntosJugadores(numCartasAleatorias);
    }

    public void calcularPuntosJugadoresOmaha(){
        this.logica.calcularPuntosJugadoresOmaha();
    }
    public void foldJug(int id){
        this.logica.foldJug(id);
    }
    public void clearPuntos(int jug){
        this.logica.clearPuntos(jug);
    }
    
    public void clearAllJugPuntos(){
        this.logica.clearAllJugPuntos();
    }
}
