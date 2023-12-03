package model;

import controller.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Logic {

    private final static String simb[] = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    private final static String palos[] = {"h", "d", "s", "c"};

    private List<Carta> board;  //Cartas del board
    private List<Carta> cartasRestantes;    //Todas las cartas sin contar con la mano de los jugadores ni las cartas presentes en el board
    private Map<Integer, Jugador> jugadores;    //Par id jugador y el jugador corrspondiente
    private List<List<Carta>> combinaciones;    //Todas las combinaciones de cartas con los elementos de cartasRestantes
    private Map<Integer, Double> equity;    //Par id jugador y el equity asociado 
    private Map<Integer, Integer> vecesGanadas; //Par id jugador y las veces que gana el pot
    private int empates = 0;
    private Controller controller;

    public Logic() {
        this.board = new ArrayList<>();
        this.jugadores = new HashMap<>();
        this.cartasRestantes = new ArrayList<>();
        this.combinaciones = new ArrayList<>();
        this.equity = new HashMap<>();
        this.vecesGanadas = new HashMap<>();
        init();
    }

    //Inicializa las 52 cartas
    private void init() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                Carta c = new Carta(simb[i], palos[j]);
                cartasRestantes.add(c);
            }
        }
    }

    //Funcion recursiva que generan todas las combinaciones posibles segun el tamaño que se escoge
    private void generarCombinaciones(List<Carta> cartas, int tamCombinacion, List<Carta> combinacionActual, int indice) {
        if (combinacionActual.size() == tamCombinacion) {
            this.combinaciones.add(new ArrayList<>(combinacionActual));
            return;
        }

        for (int i = indice; i < cartas.size(); i++) {
            combinacionActual.add(cartas.get(i));
            generarCombinaciones(cartas, tamCombinacion, combinacionActual, i + 1);
            combinacionActual.remove(combinacionActual.size() - 1);
        }
    }

    //Calcula el combo de una mano
    private Jugada evalue(List<Carta> mano) {
        //Combina las cartas del board con las de la mano
        List<Carta> cartas = new SortedArrayList<>();
        cartas.addAll(mano);

        Jugada jugada;

        //Comprueba si se forma alguna de las siguientes jugadas
        if ((jugada = EscaleraColor(cartas)) != null) {
            return jugada;
        } else if ((jugada = Poker(cartas)) != null) {
            return jugada;
        } else if ((jugada = FullHouse(cartas)) != null) {
            return jugada;
        } else if ((jugada = Flush(cartas)) != null) {
            return jugada;
        } else if ((jugada = Escalera(cartas)) != null) {
            return jugada;
        } else if ((jugada = Trio(cartas)) != null) {
            return jugada;
        } else if ((jugada = DoblePareja(cartas)) != null) {
            return jugada;
        } else if ((jugada = Pareja(cartas)) != null) {
            return jugada;
        }

        return new Jugada(cartas, tJugada.CARTA_ALTA);
    }

    //Compara 2 jugadas, devuelve true si la jugada de la iz es mejor
    public boolean esMejorJugada(Jugada iz, Jugada dr) {
        boolean esMejor = false;
        List<Carta> j1 = iz.getCartas();
        List<Carta> j2 = dr.getCartas();

        for (int i = 0; i < iz.getCartas().size(); i++) {
            //Si la Jugada1, su i-iesima carta es mejor que la Jugada2
            if (j1.get(i).getVal() > j2.get(i).getVal()) {
                return true;
            }
        }
//        }

        return esMejor;
    }

    //Compara si 2 jugadas son identicas
    public boolean esIgualJugada(Jugada iz, Jugada dr) {
        boolean esIgual = true;

        List<Carta> j1 = iz.getCartas();
        List<Carta> j2 = dr.getCartas();

        for (int i = 0; i < iz.getCartas().size(); i++) {
            if (j1.get(i).getVal() != j2.get(i).getVal()) {
                return false;
            }
        }
        return esIgual;
    }

    //Devuelve la lista de ids de los jugadores que puntuan 
    public List<Integer> puntuaJugadores(Map<Integer, Jugada> jugadas) {
        List<Integer> idJugadores = new ArrayList<>(); //Lista de ids a devolver
        List<Integer> aux = new ArrayList<>();  //Lista auxiliar para calcular los ids
        tJugada mejorJugada = null;

        //Bucle para comprobar que jugador/es tienen la mejor jugador
        for (Map.Entry<Integer, Jugada> entrada : jugadas.entrySet()) {
            Integer idJugador = entrada.getKey();
            tJugada jugada = entrada.getValue().getJugada();
            //Si la jugada actual es mejor que la mejorJugada
            if (mejorJugada == null) {
                aux.add(idJugador);
                mejorJugada = jugada;
            } else if (jugada.compareTo(mejorJugada) > 0) {
                aux.clear();
                aux.add(idJugador);
                mejorJugada = jugada;
            } //Si es igual que la mejor jugada
            else if (jugada.compareTo(mejorJugada) == 0) {
                aux.add(idJugador);
            }
        }

        //En caso de que varios jugadores tuvieran la misma jugada, ver quien/es ganan 
        Jugada jugadaActual = null;
        if (aux.size() > 1) {
            for (int id : aux) {
                Jugada jugada = jugadas.get(id);
//                System.out.println(jugada.getCartas());
                if (jugadaActual == null) {
                    jugadaActual = jugada;
                    idJugadores.add(id);
                } else if (esMejorJugada(jugada, jugadaActual)) {
                    idJugadores.clear();
                    jugadaActual = jugada;
                    idJugadores.add(id);
                } else if (esIgualJugada(jugada, jugadaActual)) {
                    idJugadores.add(id);
                }
            }

        } else {
            idJugadores.add(aux.get(0));
        }

        //Para comproabr cuantas veces se ganan sin empate
        if (idJugadores.size() == 1) {
            if (!this.vecesGanadas.containsKey(idJugadores.get(0))) {
                this.vecesGanadas.put(idJugadores.get(0), 1);
            } else {
                this.vecesGanadas.put(idJugadores.get(0), this.vecesGanadas.get(idJugadores.get(0)) + 1);
            }
        } else if (idJugadores.size() > 1) {
            this.empates++;
        }
        return idJugadores;
    }

    //Calcular los puntos para cada jugador y sumarselos
    public void calcularPuntosJugadores(int numCartasAleatorias) {
        this.combinaciones.clear(); //Vaciar la lista de combinaciones 
        List<Carta> combinacionActual = new ArrayList<>();  //Lista auxiliar para la funcion recursiva

        //Generar las combinaciones segun el numero de cartas aleatorias, si se escoge en 5 en 5, de 3 en 3, simulando las distintas fases de la partida, preflop, river...
        generarCombinaciones(this.cartasRestantes, numCartasAleatorias, combinacionActual, 0);
        
        int numComb = 0;

        //Para cada posible combinacion
        for (List<Carta> combinacion : this.combinaciones) {
           
            numComb++;

            //La lista de cartas, sumando las del board y las del jugador
            List<Carta> cartas = new ArrayList<>();

            //Mapa id jugador y la mejor jugada encontrada
            Map<Integer, Jugada> jugadas = new HashMap<>();

            for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
                Integer idJugador = entrada.getKey();  //Id del jugador
                List<Carta> manoJugador = entrada.getValue().getCartas();   //Mano inicial del jugador

                cartas.addAll(this.board);
                //Inserta todas las cartas del board de manera ordenada
                cartas.addAll(combinacion);
                //Inserta las cartas de la mano del jugador de manera ordenada
                cartas.addAll(manoJugador);
                //Listo para ver si forma alguna jugada
                Jugada jugada = evalue(cartas);
                jugadas.put(idJugador, jugada);

                //Borrar las cartas para la siguiente iteracion
                cartas.clear();
            }

            //Lista de jugadores que se hay que puntuar             
            List<Integer> idJugadores = puntuaJugadores(jugadas);

            double puntos = 1.00 / idJugadores.size();
            //Sumar puntos a los jugadores
            for (Integer id : idJugadores) {
                jugadores.get(id).sumaPuntos(puntos);
            }
        }
//
//        System.out.println("Numero de combos totales: " + numComb);
//
//        for (Map.Entry<Integer, Integer> entrada : this.vecesGanadas.entrySet()) {
//            System.out.println("Jugador " + entrada.getKey() + " : " + entrada.getValue());
//        }
//
//        System.out.println("Numero de empates: " + this.empates);

    }

    //calcular equity para cada jugador
    public void calculateEquity() {
        int puntosTotales = 0;
        for (Integer jug : jugadores.keySet()) {
            puntosTotales += jugadores.get(jug).getPuntos();
        }
        for (Integer jug : jugadores.keySet()) {
            this.equity.put(jug, (jugadores.get(jug).getPuntos() / puntosTotales) * 100);
        }
    }

    //devolver todos los porcenttajes de todos los jugadores
    public Map<Integer, Double> getEquity() {
        calculateEquity();
        return this.equity;
    }

//    //Elimina las cartas duplicadas 
//    public void eliminarRep() {
//        //Eliminar cartas que ya han aparecido en el board del total de cartas
//        cartasRestantes.removeAll(board);
//
//        //Bucle para eliminar del total, las manos iniciales de cada jugador
//        for (int i = 0; i < 6; i++) {
//            cartasRestantes.removeAll(jugadores.get(i).getCartas());
//        }
//    }
    //Introducir carta al board
    public void addBoard(Carta c) {
        this.board.add(c);
        this.cartasRestantes.remove(c);
    }

    //Quitar una carta del board
    public void removeBoard(Carta c) {
        this.board.remove(c);
        this.cartasRestantes.add(c);
    }

    //Devuelve una carta aleatorio que no ha salido todavia
    public Carta getRandomCarta() {
        Random rand = new Random();
        int indRandom = rand.nextInt(cartasRestantes.size());
        Carta c = cartasRestantes.get(indRandom);
        return c;
    }

    //Inicializa nuevo jugador con sus cartas
    public void enterJugCard(int idJugador, String carta) {
        String c[] = carta.split(",");
        List<Carta> card = new ArrayList<>();
        Carta card1 = new Carta(c[0].substring(0, 1), c[0].substring(1, 2));
        Carta card2 = new Carta(c[1].substring(0, 1), c[1].substring(1, 2));
        card.add(card1);
        card.add(card2);
        cartasRestantes.remove(card1);
        cartasRestantes.remove(card2);
        jugadores.put(idJugador, new Jugador(card, idJugador));
    }
    
    //fold del jugador id
    public void foldJug(int id){
        jugadores.remove(id);
        equity.remove(id);
    }

    //Inicializar el board
    public void enterBoardCard(String carta) {
        String c[] = carta.split(",");
        for (String c1 : c) {
            Carta card = new Carta(c1.substring(0, 1), c1.substring(1, 2));
            board.add(card);
            cartasRestantes.remove(card);
        }

    }

    //Insertar cartas random al jugador seleccionado
    public void randomJugCard(int jugador) {
        List<Carta> c = new ArrayList<>();
        Carta carta1 = getRandomCarta();
        c.add(carta1);
        this.cartasRestantes.remove(carta1);
        Carta carta2 = getRandomCarta();
        c.add(carta2);
        this.cartasRestantes.remove(carta2);
        jugadores.put(jugador, new Jugador(c, jugador));
    }

    //Insertar nueva carta random en el board
    public void randomBoardCard() {
        Carta carta = getRandomCarta();
        addBoard(carta);
        this.cartasRestantes.remove(carta);
    }

    //Devuelve las cartas de un jugador
    public List<Carta> getJugadorCarta(int jug) {
        return jugadores.get(jug).getCartas();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    //Devuelve las cartas board
    public List<Carta> getBoardCard() {
        return board;
    }

    //Devuelve la mejor escalera de color (Funciona)
    private Jugada EscaleraColor(List<Carta> c) {
        Jugada escaleraColor = null;

        int i = 0;
        while (i < c.size()) {
            ArrayList<Carta> tmp = new ArrayList<>(); //Lista que guarda las carta forma la escalera de color
            tmp.add(c.get(i));  //Inserta la primera carta a partir de la cual empieza la busqueda
            String palo = c.get(i).getPalo();   //El palo que se busca         
            int cur = c.get(i).getVal();    //La carta sobre la que se empieza la busqueda

            int j = i + 1;
            while (j < c.size()) {
                //Si es del mismo valo y su diferencia vale 1
                if (cur - c.get(j).getVal() == 1 && palo.equals(c.get(j).getPalo())) {
                    tmp.add(c.get(j));   //Se inserta en la lista
                    cur = c.get(j).getVal();    //Se actualiza el ultimo valor
                }

                //Si la jugada llega a tener 5 cartas => Escalera Color
                if (tmp.size() == 5) {
                    escaleraColor = new Jugada(tmp, tJugada.ESCALERA_COLOR);
                    return escaleraColor;
                }

                ++j;
            }

            ++i;
        }

        return escaleraColor;
    }

    //Devuelve la mejor escalera (Funciona)
    private Jugada Escalera(List<Carta> c) {
        Jugada escalera = null;

        int i = 0;
        while (i < c.size()) {
            ArrayList<Carta> tmp = new ArrayList<>(); //Lista que guarda las carta forma la escalera de color
            tmp.add(c.get(i));  //Inserta la primera carta a partir de la cual empieza la busqueda       
            int cur = c.get(i).getVal();    //Valor de la ultima carta que se tiene para formar la jugada

            int j = i + 1;
            while (j < c.size()) {
                //Si es del mismo valo y su diferencia vale 1
                if (cur - c.get(j).getVal() == 1) {
                    tmp.add(c.get(j));   //Se inserta en la lista
                    cur = c.get(j).getVal();    //Se actualiza el ultimo valor
                }

                //Si la jugada llega a tener 5 cartas => Escalera 
                if (tmp.size() == 5) {
                    escalera = new Jugada(tmp, tJugada.ESCALERA);
                    return escalera;
                }

                ++j;
            }

            ++i;
        }

        return escalera;
    }

    //Devuelve el mejor quad (Funciona)
    public Jugada Poker(List<Carta> c) {
        Jugada poker = null;

        int i = 0;
        while (i < c.size()) {
            ArrayList<Carta> tmp = new ArrayList<>(); //Lista que guarda las carta forma la escalera de color
            tmp.add(c.get(i));  //Inserta la primera carta a partir de la cual empieza la busqueda       
            Carta cur = c.get(i);    //Valor de la ultima carta que se tiene para formar la jugada

            int j = i + 1;
            while (j < c.size()) {
                //Si las 2 cartas es tienen el mismo valor
                if (cur.getSimb().equals(c.get(j).getSimb())) {
                    tmp.add(c.get(j));   //Se inserta en la lista
                }

                //Si la jugada llega a tener 4 cartas iguales => quad
                if (tmp.size() == 4) {
                    poker = new Jugada(tmp, tJugada.POKER);
                    return poker;
                }

                ++j;
            }

            ++i;
        }

        return poker;
    }

    //Devuelve el mejor full house (Funciona)
    public Jugada FullHouse(List<Carta> c) {
        Jugada fullhouse = null;

        Jugada trio = Trio(c); //Devuelve el mejor trio

        List<Carta> tmp = new ArrayList<>(c);

        if (trio != null) {
            tmp.removeAll(trio.getCartas().subList(0, 3));

            Jugada pareja = Pareja(tmp);

            if (pareja != null) {
                List<Carta> tmp2 = new ArrayList<>();
                tmp2.addAll(trio.getCartas().subList(0, 3));
                tmp2.addAll(pareja.getCartas().subList(0, 1));
                fullhouse = new Jugada(tmp2, tJugada.FULL_HOUSE);
            }

        }

        return fullhouse;
    }

    //Devuelve el mejor Flush (Funciona)
    public Jugada Flush(List<Carta> c) {
        Jugada flush = null;

        //Contador para cartas de cada palo
        int contH = 0;
        int contD = 0;
        int contC = 0;
        int contS = 0;

        String palo = null; //El palo que primero consigue sus 5 cartas

        int i = 0;

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
                break;
            } else if (contD == 5) {
                palo = "d";
                break;
            } else if (contC == 5) {
                palo = "c";
                break;
            } else if (contS == 5) {
                palo = "s";
                break;
            }
            i++;

        }

        //Comprobar si hay flush
        if (contH == 5 || contD == 5 || contC == 5 || contS == 5) {
            //Lista auxiliar para almacenar valores del flush
            ArrayList<Carta> lista = new ArrayList<>();

            for (Carta carta : c) {
                if (carta.getPalo().equals(palo)) {
                    lista.add(carta);
                }

                if (lista.size() == 5) {
                    flush = new Jugada(lista, tJugada.COLOR);
                    return flush;
                }
            }

        }

        return flush;
    }

    //Devuelve el mejor trio (Funciona)
    public Jugada Trio(List<Carta> c) {
        Jugada trio = null;

        int i = 0;
        while (i < c.size()) {
            List<Carta> tmp = new ArrayList<>();
            int cur = c.get(i).getVal();
            tmp.add(c.get(i));

            int j = i + 1;
            while (j < c.size()) {
                int sig = c.get(j).getVal();

                if (cur == sig) {
                    tmp.add(c.get(j));
                }

                //Si ya hay 3 cartas iguales
                if (tmp.size() == 3) {
                    trio = new Jugada(tmp, tJugada.TRIO);
                    return trio;

                }

                ++j;
            }

            ++i;          
        }
        
        return trio;
    }

    //Devuelve la mejor doble pareja (Funciona)
    public Jugada DoblePareja(List<Carta> c) {
        Jugada doblePareja = null;

        List<Carta> aux = new ArrayList<>(c);
        List<Carta> aux2 = new ArrayList<>();
        boolean ok = false;

        //Se busca la primera pareja
        int i = 0;
        while (i < c.size() - 1) {
            Carta cur = c.get(i);
            Carta sig = c.get(i + 1);
            if (cur.getVal() == sig.getVal()) {
                // eliminamos la primera pareja
                aux.remove(cur);
                aux.remove(sig);
                //Mete la pareja de carta al principio de la jugada               
                aux2.add(cur);
                aux2.add(sig);
                ok = true;
                break;
            }
            i++;
        }

        int j = 0;

        while (j < aux.size() - 1 && ok) {
            Carta cur = aux.get(j);
            Carta sig = aux.get(j + 1);

            if (cur.getVal() == sig.getVal()) {
                
                aux.remove(cur);
                aux.remove(sig);

                aux2.add(cur);
                aux2.add(sig);
                doblePareja = new Jugada(aux2, tJugada.DOBLE_PAREJA);
                break;
            }
            j++;
        }
        
        return doblePareja;
    }

    //Devuelve la mejor pareja (Funciona)
    public Jugada Pareja(List<Carta> c) {
        Jugada pareja = null;
        int i = 0;
        while (i < c.size() - 1) {
            Carta cur = c.get(i);
            Carta sig = c.get(i + 1);
            if (cur.getVal() == sig.getVal()) {
                List<Carta> aux = new ArrayList<>();
                
                //La pareja
                aux.add(cur);
                aux.add(sig); 
                pareja = new Jugada(aux, tJugada.PAREJA);
                break;
            }
            i++;
        }

        return pareja;
    }
    
    public void clearPuntos(int jug){
        jugadores.get(jug).clearPuntos();
    }
    public void clearAllJugPuntos(){
        for(int jug:this.jugadores.keySet()){
            clearPuntos(jug);
        }
    }
}
