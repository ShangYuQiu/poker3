package model;

import controller.Controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Logic {

    private final static String simb[] = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    private final static String palos[] = {"h", "d", "s", "c"};

    private List<Carta> board;  //Cartas del board
    private List<Carta> cartasRestantes;    //Todas las cartas sin contar con las de las manos de los jugadores ni el del board
    private Map<Integer, Jugador> jugadores;    //El numero de jugador y su mano de cartas
    private List<List<Carta>> combinaciones;    //Todas las combinaciones de cartas con los elementos de cartasRestantes
    private Map<Integer, Double> equity;
    private Controller controller;

    public Logic() {
        this.board = new ArrayList<>();
        this.jugadores = new HashMap<>();
        this.cartasRestantes = new ArrayList<>();
        this.combinaciones = new ArrayList<>();
        this.equity=new HashMap<>();
        init();
    }

    //Inicializa las 52 cartas
    public void init() {
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

        for (int i = 0; i < 5; ++i) {
            //Si la Jugada1, su i-iesima carta es mejor que la Jugada2
            if (j1.get(i).getVal() > j2.get(i).getVal()) {
                return true;
            }
        }

        return esMejor;
    }

    //Compara si 2 jugadas son identicas
    public boolean esIgualJugada(Jugada iz, Jugada dr) {
        boolean esIgual = true;

        List<Carta> j1 = iz.getCartas();
        List<Carta> j2 = dr.getCartas();

        for (int i = 0; i < 5; ++i) {
            if (j1.get(i).getVal() != j2.get(i).getVal()) {
                return false;
            }
        }
        return esIgual;
    }

    //Devuelve la lista de ids de los jugadores que puntuan 
    public List<Integer> puntuaJugadores(Map<Integer, Jugada> jugadas) {
        List<Integer> idJugadores = new ArrayList<>();
        List<Integer> idJug = new ArrayList<>();
        tJugada mejorJugada = null;

        //Bucle para comprobar que jugador/es tienen la mejor jugador
        for (Map.Entry<Integer, Jugada> entrada : jugadas.entrySet()) {
            Integer idJugador = entrada.getKey();
            tJugada jugada = entrada.getValue().getJugada();

            //Si la jugada actual es mejor que la mejorJugada
            if (mejorJugada == null) {
                idJugadores.add(idJugador);
                idJug.add(idJugador);
                mejorJugada = jugada;
            } else if (jugada.compareTo(mejorJugada) > 0) {
                idJugadores.clear();
                idJugadores.add(idJugador);
                idJug.add(idJugador);
                mejorJugada = jugada;
            } //Si es igual que la mejor jugada
            else if (jugada.compareTo(mejorJugada) == 0) {
                idJugadores.add(idJugador);
                idJug.add(idJugador);
            }
        }

        //En caso de que varios jugadores tuvieran la misma jugada, ver quien/es ganan 
        Jugada jugadaActual = null;
        if (idJug.size() > 1) {
            for (int id : idJug) {
                Jugada jugada = jugadores.get(id).getJugada();

                if (jugadaActual == null) {
                    jugadaActual = jugada;
                    idJugadores.add(id);
                }
                else if(esMejorJugada(jugada, jugadaActual)){
                    idJugadores.clear();
                    jugadaActual = jugada;
                    idJugadores.add(id);
                }
                else if(esIgualJugada(jugada, jugadaActual)){
                    idJugadores.add(id);
                }
            }
        }

        return idJugadores;
    }

    //Calcular los puntos para cada jugador y sumarselos
    public void calcularPuntosJugadores(int numCartasAleatorias) {
        this.combinaciones.clear(); //Vaciar la lista de combinaciones 
        List<Carta> combinacionActual = new ArrayList<>();  //Lista auxiliar para la funcion recursiva

        //Generar las combinaciones segun el numero de cartas aleatorias, si se escoge en 5 en 5, de 3 en 3, simulando las distintas fases de la partida, preflop, river...
        generarCombinaciones(this.cartasRestantes, numCartasAleatorias, combinacionActual, 0);

        //Para cada posible combinacion
        for (List<Carta> combinacion : this.combinaciones) {

            //La lista de cartas, sumando las del board y las del jugador
            List<Carta> cartas = new SortedArrayList<>();

            //Mapa id jugador y la mejor jugada encontrada
            Map<Integer, Jugada> jugadas = new HashMap<>();

            for (Map.Entry<Integer, Jugador> entrada : this.jugadores.entrySet()) {
                Integer idJugador = entrada.getKey();  //Id del jugador
                List<Carta> manoJugador = entrada.getValue().getCartas();   //Mano inicial del jugador

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

    }
    //calcular equity para cada jugador
    public void calculateEquity(){
        int puntosTotales=0;
        for(int i=0;i<6;i++){
            //calcular puntuacon total
            puntosTotales+=jugadores.get(i).getPuntos();
        }
        for(int i=0;i<6;i++){
            //guardar los porcentajes de todos los jugadores
            equity.put(i, (jugadores.get(i).getPuntos()/puntosTotales)*100);
        }
    }
    //devolver todos los porcenttajes de todos los jugadores
    public Map<Integer,Double> getEquity(){
        calculateEquity();
        return equity;
    }
    
    //Elimina las cartas duplicadas 
    public void eliminarRep() {
        //Eliminar cartas que ya han aparecido en el board del total de cartas
        cartasRestantes.removeAll(board);

        //Bucle para eliminar del total, las manos iniciales de cada jugador
        for (int i = 0; i < 6; i++) {
            cartasRestantes.removeAll(jugadores.get(i).getCartas());
        }
    }

    //Introducir carta al board
    public void addBoard(Carta c) {
        this.board.add(c);
    }

    //Quitar una carta del board
    public void removeBoard(Carta c) {
        this.board.remove(c);
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


    /*Métodos para calcular jugadas*/
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

    private Jugada EscaleraColor(List<Carta> c) {
        Jugada escaleraColor = null;

        int i = 0;
        while (i < c.size()) {
            ArrayList<Carta> tmp = new ArrayList<>(); //Lista que guarda las carta forma la escalera de color
            tmp.add(c.get(i));  //Inserta la primera carta a partir de la cual empieza la busqueda
            String palo = c.get(i).getPalo();   //El palo que se busca         
            int cur = c.get(i).getVal();    //Valor de la ultima carta que se tiene para formar la jugada

            int j = i + 1;
            while (j < c.size()) {
                //Si es del mismo valo y su diferencia vale 1
                if (cur - c.get(j).getVal() == 1 && palo.equals(c.get(j).getPalo())) {
                    tmp.add(0, c.get(j));   //Se inserta en la lista
                    cur = c.get(j).getVal();    //Se actualiza el ultimo valor
                }
                ++j;
            }

            //Si la jugada llega a tener 5 cartas => Escalera Color
            if (tmp.size() == 5) {
                //String msgJugada = String.format("Straight Flush with %s", getStrCartas());
                escaleraColor = new Jugada(tmp, tJugada.ESCALERA_COLOR);
                break;
            }
            ++i;
        }

        return escaleraColor;
    }

    //Comprueba si hay escalera
    public Jugada Escalera(List<Carta> c) {
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
                ++j;
            }

            //Si la jugada llega a tener 5 cartas => Escalera 
            if (tmp.size() >= 5) {
                //tmp.removeAll(this.board);
                if (!tmp.isEmpty()) {
                    escalera = new Jugada(tmp, tJugada.ESCALERA);
                    break;
                }
            }
            ++i;
        }

        return escalera;
    }

    //Comprueba si hay poker
    private Jugada Poker(List<Carta> c) {
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
                ++j;
            }
            //Si la jugada llega a tener al menos 4 cartas => quad
            if (tmp.size() >= 4) {
                if (!tmp.isEmpty()) {
                    poker = new Jugada(tmp, tJugada.POKER);
                    break;
                }
            }
            ++i;
        }

        return poker;
    }

    //Comprueba si hay full house
    private Jugada FullHouse(List<Carta> c) {
        Jugada fullHouse = null;
        boolean trio = false;
        List<Carta> tmp = new ArrayList<>(c);
        //Lista auxiliar que almacenan las cartas que forman el Full House
        ArrayList<Carta> lista = new ArrayList<>();
        int cont = 1;
        int i = 0;

        while (i < tmp.size() - 1) {
            int cur = tmp.get(i).getVal();
            int sig = tmp.get(i + 1).getVal();

            if (cur == sig) {
                cont++;
                if (cont == 3) {
                    lista.add(tmp.get(i - 1));
                    lista.add(tmp.get(i));
                    lista.add(tmp.get(i + 1));
                    trio = true;
                    cont = 1;
                }
            } else {//si se corta en medio                             
                if (cont == 2) {

                    lista.add(tmp.get(i - 1));
                    lista.add(tmp.get(i));
                }
                cont = 1;
            }

            if (i == tmp.size() - 2 && cont == 2) {
                lista.add(tmp.get(i - 1));
                lista.add(tmp.get(i));
            }
            i++;
        }

        if (lista.size() > 4 && trio) {
            if (!lista.isEmpty()) {
                fullHouse = new Jugada(tmp, tJugada.FULL_HOUSE);
            }
        }
        return fullHouse;
    }

    //Devuelve el mejor Flush (Funciona)
    private Jugada Flush(List<Carta> c) {
        Jugada flush = null;
        Collections.sort(c);

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
                if (c.get(j).getPalo().equals(palo)) {
                    lista.add(c.get(j));
                }
            }

            flush = new Jugada(lista, tJugada.COLOR);
        }

        return flush;
    }

    //Comprueba si hay trio
    public Jugada Trio(List<Carta> c) { // return lista 
        Jugada trio = null;
        int i = 0;
        int cont = 1;   //Numero de cartas del trio actual
        List<Carta> trios = new ArrayList<>();

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
            //Si hay posibilidad de trio
            if (cont == 3) {
                //Almacenamos las cartas que forman el trio
                trios.add(c.get(i - 1));
                trios.add(c.get(i));
                trios.add(c.get(i + 1));
                //quitamos de la lista de trios las cartas de board
                trio = new Jugada(trios, tJugada.TRIO);
                break;
            }
            i++;
        }
        return trio;
    }

    // to do
    //Devuelve la mejor doble pareja (Funciona)
    private Jugada DoblePareja(List<Carta> c) {
        Jugada doblePareja = null;
        Collections.sort(c);
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
                //String msgJugada = String.format("%s with %s ", "Two pairs", getStrCartas());
                doblePareja = new Jugada(c, tJugada.DOBLE_PAREJA);
            }
        }
        return doblePareja;
    }

    //Devuelve la mejor pareja (Funciona)
    private Jugada Pareja(List<Carta> c) {
        Jugada pareja = null;
        Collections.sort(c);

        int i = 0;
        while (i < c.size() - 1) {
            int cur = c.get(i).getVal();
            int sig = c.get(i + 1).getVal();
            if (cur == sig) {
                //Mete la pareja de carta al principio de la jugada
                List<Carta> aux = new ArrayList<>();
                aux.add(c.get(cur));
                aux.add(c.get(sig));
                //Forma la cadena de la jugada, por ejemplo: "A pair of Ases with AhAh7h6c2d"
                // String msgJugada = String.format("Pair of %s with %s", Evaluador.msg.get(cur - 2), getStrCartas());
                pareja = new Jugada(aux, tJugada.PAREJA);
                break;
            }
            i++;
        }

        return pareja;
    }
}
