
package model;

import controller.Controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;
import model.*;
import controller.*;


public class Logica {
    
    private static String simb[] = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static String palos[] = {"h", "d", "s", "c"};
    
    private List<Carta> board;
    private List<Carta> allCarta;
    private Map<Integer,List<Carta>> jug;
    private Controller controller;
    public Logica (){
        board = new SortedArrayList<>();
        jug = new  HashMap<>();
        allCarta=new ArrayList<>();
        init();
    }
    
    public void init(){
        
        for (int i  = 0; i < 13; i++){
        
            for (int j = 0; j < 4; j++){
            
                Carta c = new Carta (simb[i], palos[j]);
                
                allCarta.add(c);
                
            }
        }
    }
    
    //revisar
    public void eliminarRep(){
        allCarta.removeAll(board);
        
        for (int i = 0; i < 6; i++){
            allCarta.removeAll(jug.get(i));
        }
        
    }
    public void addBoard(Carta c){
        this.board.add(c);
    }
    
    public void removeBoard (Carta c){
        this.board.remove(c);
    }
    
    public Carta getRandomCarta(){
        Random rand = new Random();
        int indRandom = rand.nextInt(allCarta.size());
        
        Carta c = allCarta.get(indRandom);
        allCarta.remove(c);
        return c;
    }
    //aniadir las cartas introducido por el jugador elegido a la lista jug
    public void enterJugCard(int jugador, String carta){
        String c[]=carta.split(",");
        List<Carta> card=new ArrayList<>();
        Carta card1=new Carta(c[0].substring(0, 1),c[0].substring(1, 2));
        Carta card2=new Carta(c[1].substring(0, 1),c[1].substring(1, 2));
        card.add(card1);
        card.add(card2);
        allCarta.remove(card1);
        allCarta.remove(card2);
        jug.put(jugador,card);
    }
    //aniadir las cartas boards introducido a la lista board
    public void enterBoardCard(String carta){
        String c[]=carta.split(",");
        for(int i=0;i<5;i++){
        Carta card=new Carta(c[i].substring(0, 1),c[i].substring(1, 2));
        board.add(card);
        allCarta.remove(card);
        }
    
    }
    //aniadir las cartas random del jugador elegido a la lista jug
    public void randomJugCard(int jugador){
            List<Carta> c=new ArrayList<Carta>();
            Carta carta1=getRandomCarta();
            c.add(carta1);
            Carta carta2=getRandomCarta();
            c.add(carta2);
            jug.put(jugador,c);
    }
    //devuelve cartas del jugador elegido
    public List<Carta> getJugadorCarta(int jugador){
        return jug.get(jugador);
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    //aniadir las cartas random del board a la lista board
    public void randomBoardCard(){
        Carta carta=getRandomCarta();
        addBoard(carta);
    }
    //devuelve las cartas board
    public List<Carta> getBoardCard(){
        return board;
    }
    //funcion que genera 5 cartas con las cartas que sobra 
    
    // mezclar esass 5 cartas con las 2 de cada jugador y evaluar cual de ellos tiene la mejor mano
    
    // poner las funciones de evaluar jugadas de la 1 o 2 pract
    
    // devolver en vez de Jugada , tJugada mejor ???
    
    /*-------------------------------------METODOS PRIVADOS-------------------------------------------*/
    //Comprobar que todas las cartas son del mismo palo 
    private boolean esMismoPalo(List<Carta> c) {
        boolean mismoPalo = true;
        int i = 0;

        while (i < c.size() - 1 && mismoPalo) {
            if (!c.get(i).getPalo().equals(c.get(i + 1).getPalo())) {
                mismoPalo = false;
            }
            i++;
        }
        return mismoPalo;
    }

    //Comprobar que todas las cartas son del mismo valor independientemente del palo
    private boolean esMismoValor(List<Carta> c) {
        boolean mismoValor = true;
        int i = 1;
        int primValor = c.get(0).getVal();
        while (i < c.size() && mismoValor) {
            if (primValor != c.get(i).getVal()) {
                mismoValor = false;
            }
            i++;
        }
        return mismoValor;
    }

    /*--------------------------------------------------------------------------------------------------*/
 /*-- METODOS PARA COMPROBAR SI CON LA MANO ACTUAL SE PUEDA FORMAR ALGUNAS DE LAS JUGADAS DEL POKER--*/
    private Jugada EscaleraColor(List<Carta> c) {
        Jugada escaleraColor = null;

        if (Escalera(c) != null && esMismoPalo(c)) {
            String msgJugada = "";
            //String msgJugada = String.format("Straight Flush with %s", this.mano.getStrCartas());
            escaleraColor = new Jugada(c, tJugada.ESCALERA_COLOR, msgJugada);
        }

        return escaleraColor;
    }

    private Jugada Escalera(List<Carta> c) {
        Jugada escalera = null;
        Collections.sort( c);
        //Distinguimos casos dependiendo de si la mano contiene Aces o no 
        List<Carta> tmp = new ArrayList<>(c);
        if (c.get(0).getSimb().equals("A")){
            Carta card = new Carta ("A",c.get(0).getPalo());
            card.setValor(1);
            tmp.add(card);

        }

        int cont = 1;
        boolean gutshot = false;
        boolean openended = false;
        boolean roto = false;
        boolean ace = false;
        int contR = 0;
       
        
        //caso especial Ace al principio
        if (tmp.get(0).getSimb().equals("A")){
            ace = true;
        }
        
        for ( int i = 0; i< tmp.size()-1;i++){

            if ( (tmp.get(i).getVal() - tmp.get(i+1).getVal()) == 1){
                cont ++;
            }
           
            //gutshot : K Q J 9 8
            else if ((tmp.get(i).getVal() - tmp.get(i+1).getVal()) == 2){
                roto = true;
                contR = cont +1;
                cont =1;
                ace = false;
               
            }
            else if ((tmp.get(i).getVal() - tmp.get(i+1).getVal()) > 2){
                roto = false;
                contR =0;
                cont=1;
                ace = false;
            }
           
            if (cont == 5){
                //String msgJugada = String.format("Straight with %s", this.mano.getStrCartas());
                String msgJugada = "";
                escalera = new Jugada(c, tJugada.ESCALERA, msgJugada);
                gutshot = false;
                roto = false;
                openended=false;
                contR =0;
                
            }
           
            else if ( cont == 4 && !ace){
                openended = true;
                
            }
           
            else if ( cont == 4 && ace){
                gutshot=true;
            }
            else if ( cont > 0 && roto && contR > 0 ){
                if ( cont + contR == 5){
                gutshot=true;
                roto = false;
                contR = 0;
                }
            }
        }
       
        if (openended == true){
             //addDraw("Draw: Straight Open ended");
        }
       
        else if ( gutshot == true){
            //addDraw("Draw: Straight Gutshot");
        }       
        
        return escalera;
    }

    //Devuelve el poker si existe (Funciona)
    private Jugada Poker(List<Carta> c) {
        Jugada poker = null;

        int i = 0;
        int cont = 1;
        ArrayList<Carta> lista = new ArrayList<>();

        while (i < c.size() - 1) {
            int cur = c.get(i).getVal();
            int sig = c.get(i + 1).getVal();
            
            if (cur == sig) {
                cont++;
            } else {
                cont = 1;
            }

            if (cont == 4) {
                int index = i - 2;

                //Quita las 4 cartas iguales
                for (int j = 0; j < 4; j++) {
                    Carta tmp = c.remove(index);
                    lista.add(0, tmp);
                }

                //Este seria el kicker (Primero de la mano (Descendente) quitado las 4 cartas iguales)
                Carta kicker = c.remove(0); 
                lista.add(0, kicker);

                for (int k = 0; k < 5; k++) {
                    Carta tmp = lista.remove(0);
                    c.add(0, tmp);
                }
                
                String msgJugada = "";
                //String msgJugada = String.format("Four of a kind (%s) with %s", Evaluador.msg.get(cur - 2), getStrCartas());
                poker = new Jugada(c, tJugada.POKER, msgJugada);
                break;
            }

            ++i;
        }

        return poker;
    }

    //Devuelve un Full House (Funciona)
    private Jugada FullHouse(List<Carta> c) {
        Jugada fullHouse = null;

        //Lista auxiliar que almacenan las cartas que forman el Full House
        ArrayList<Carta> lista = new ArrayList<>();

        if (Trio(c) != null) {
            lista.add(0, c.remove(0));
            lista.add(0, c.remove(0));
            lista.add(0, c.remove(0));

            if (Pareja(c) != null) {
                lista.add(0, c.remove(0));
                lista.add(0, c.remove(0));

                for (int i = 0; i < 5; ++i) {
                    Carta tmp = lista.remove(0);
                    c.add(0, tmp);
                }
                String msgJugada = "";
                //String msgJugada = String.format("Full House with %s", getStrCartas());
                fullHouse = new Jugada(c, tJugada.FULL_HOUSE, msgJugada);
            }

        }
        return fullHouse;
    }

    //Devuelve el mejor Flush (Funciona)
    private Jugada Flush(List<Carta> c) {
        Jugada flush = null;

        //Contador para cartas de cada palo
        int contH = 0;
        int contD = 0;
        int contC = 0;
        int contS = 0;

        String palo = null; //El palo que primero consigue sus 5 cartas

        int i = 0;
        int index = c.size() - 1; //Indice hasta la cual ya hay 5 cartas del mismo palo

        while (i < c.size()) {
            //Contamos los palos
            switch (c.get(i).getPalo()) {
                case "h" ->
                    contH++;
                case "d" ->
                    contD++;
                case "c" ->
                    contC++;
                case "s" ->
                    contS++;
            }

            //Identifica que palo tiene ya sus 5 cartas
            if (contH == 5) {
                palo = "h";
                index = i;
                break;
            } else if (contD == 5) {
                palo = "d";
                index = i;
                break;
            } else if (contC == 5) {
                palo = "c";
                index = i;
                break;
            } else if (contS == 5) {
                palo = "s";
                index = i;
                break;
            }
            i++;

        }

        //Si hay flush
        if (contH > 4 || contD > 4 || contC > 4 || contS > 4) {
            //Lista auxiliar para almacenar valores del flush
            ArrayList<Carta> lista = new ArrayList<>();

            //Recorrido en sentido inverso desde index
            for (int j = index; j >= 0; --j) {
                if (c.get(j).getPalo().equals(palo)){
                    lista.add (c.get(j));
                }
            }

            //Extraen los valores de flush y los inserta al incio de la mano
            flush = new Jugada(lista, tJugada.COLOR, "Flush");
        } //No hay Flush pero si draw
        else if (contH == 4 || contD == 4 || contC == 4 || contS == 4) {
            //addDraw("Draw: Flush");
        }

        return flush;
    }

    //Devuelve el mejor trio (Funciona)
    private Jugada Trio(List<Carta> c) {
        Jugada trio = null;
        int i = 0;
        int cont = 1;   //Numero de cartas del trio actual

        while (i < c.size() - 1) {
            int cur = c.get(i).getVal();
            int sig = c.get(i + 1).getVal();

            //Contamos si la actual es igual a la siguiente
            if (cur == sig) {
                cont++;
            } //Contamos de nuevo
            else {
                cont = 1;
            }

            //Si hay trio
            if (cont == 3) {
                //Quitamos esas cartas de la mano para insertarlas al inicio 
                int index = i - 1;
                Carta tmp = c.remove(index);
                Carta tmp2 = c.remove(index);
                Carta tmp3 = c.remove(index);
                //Los insertamos de esta manera para que se mantenga el orden relativo
                c.add(0, tmp3);
                c.add(0, tmp2);
                c.add(0, tmp);
                //
                String msgJugada = "";
                //String msgJugada = String.format("Three of a kind (%s) with %s", Evaluador.msg.get(cur - 2), getStrCartas());
                trio = new Jugada(c, tJugada.TRIO, msgJugada);
                break;
            }
            i++;
        }
        return trio;
    }

    //Devuelve la mejor doble pareja (Funciona)
    private Jugada DoblePareja(List<Carta> c) {
        Jugada doblePareja = null;

        //Se busca la primera pareja
        if (Pareja(c) != null) {
            //Los quitamos de la lista
            Carta tmp = c.remove(0);
            Carta tmp2 = c.remove(0);

            //Si se encuentra una segunda pareja
            if (Pareja(c) != null) {
                //Se insertan la primera pareja en la mano
                c.add(0, tmp2);
                c.add(0, tmp);
                String msgJugada = "";
                //String msgJugada = String.format("%s with %s ", "Two pairs", getStrCartas());
                doblePareja = new Jugada(c, tJugada.DOBLE_PAREJA, msgJugada);
            }
        }
        return doblePareja;
    }

    //Devuelve la mejor pareja (Funciona)
    private Jugada Pareja(List<Carta> c) {
        Jugada pareja = null;

        int i = 0;
        while (i < c.size() - 1) {
            int cur = c.get(i).getVal();
            int sig = c.get(i + 1).getVal();
            if (cur == sig) {
                //Mete la pareja de carta al principio de la jugada
                Carta tmp = c.remove(i);
                Carta tmp2 = c.remove(i);
                c.add(0, tmp);
                c.add(1, tmp2);

                //Forma la cadena de la jugada, por ejemplo: "A pair of Ases with AhAh7h6c2d"
                String msgJugada = "";
                //String msgJugada = String.format("Pair of %s with %s", Evaluador.msg.get(cur - 2), getStrCartas());
                pareja = new Jugada(c, tJugada.PAREJA, msgJugada);
                break;
            }
            i++;
        }

        return pareja;
    }
}
