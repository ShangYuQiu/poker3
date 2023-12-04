package poker3;

import controller.Controller;
import gui.MainFrame;
import model.Logic;

public class Poker3 {

    public static void main(String[] args) {
        Logic logic = new Logic();
        MainFrame frame = new MainFrame();
        Controller controller = new Controller(logic, frame);
        logic.setController(controller);
        frame.setController(controller);
        frame.setVisible(true);

//        List<Carta> l0 = new ArrayList<>();
//        l0.add(new Carta("A", "d"));
//        l0.add(new Carta("A", "c"));
//        l0.add(new Carta("3", "s"));
//        l0.add(new Carta("3", "c"));
//        l0.add(new Carta("4", "c"));
//
//        Jugada j0 = new Jugada(l0, tJugada.DOBLE_PAREJA);
//
//        List<Carta> l1 = new ArrayList<>();
//        l1.add(new Carta("Q", "h"));
//        l1.add(new Carta("Q", "d"));
//        l1.add(new Carta("3", "s"));
//        l1.add(new Carta("3", "c"));
//        l1.add(new Carta("4", "c"));
//
//        Jugada j1 = new Jugada(l1, tJugada.DOBLE_PAREJA);
//
//        List<Carta> l2 = new ArrayList<>();
//        l2.add(new Carta("3", "s"));
//        l2.add(new Carta("3", "c"));
//        l2.add(new Carta("2", "d"));
//        l2.add(new Carta("2", "c"));
//        l2.add(new Carta("A", "s"));
//
//        Jugada j2 = new Jugada(l2, tJugada.DOBLE_PAREJA);
//
//        List<Carta> l3 = new ArrayList<>();
//        l3.add(new Carta("3", "s"));
//        l3.add(new Carta("3", "c"));
//        l3.add(new Carta("2", "d"));
//        l3.add(new Carta("2", "c"));
//        l3.add(new Carta("K", "c"));
//
//        Jugada j3 = new Jugada(l3, tJugada.DOBLE_PAREJA);
//
//        List<Carta> l4 = new ArrayList<>();
//        l4.add(new Carta("3", "s"));
//        l4.add(new Carta("3", "c"));
//        l4.add(new Carta("2", "d"));
//        l4.add(new Carta("2", "c"));
//        l4.add(new Carta("7", "c"));
//
//        Jugada j4 = new Jugada(l4, tJugada.DOBLE_PAREJA);
//
//        List<Carta> l5 = new ArrayList<>();
//        l5.add(new Carta("8", "d"));
//        l5.add(new Carta("8", "h"));
//        l5.add(new Carta("3", "s"));
//        l5.add(new Carta("3", "c"));
//        l5.add(new Carta("4", "c"));
//
//        Jugada j5 = new Jugada(l5, tJugada.DOBLE_PAREJA);
//
//        List<Integer> aux = new ArrayList<>();
//        aux.addAll(Arrays.asList(0,1,2,3,4,5));
//        
//        
//        Map<Integer, Jugada> jugadas = new HashMap<>();
//        jugadas.put(0, j0);
//        jugadas.put(1, j1);
//        jugadas.put(2, j2);
//        jugadas.put(3, j3);
//        jugadas.put(4, j4);
//        jugadas.put(5, j5);
//        
//        
//        List<Integer> idJugadores = new ArrayList<>();
//        Jugada jugadaActual = null;
//        for (int id : aux) {
//            Jugada jugada = jugadas.get(id);
////                System.out.println(jugada.getCartas());
//            if (jugadaActual == null) {
//                jugadaActual = jugada;
//                idJugadores.add(id);
//            } else if (logic.esMejorJugada(jugada, jugadaActual)) {
//                idJugadores.clear();
//                jugadaActual = jugada;
//                idJugadores.add(id);
//            } else if (logic.esIgualJugada(jugada, jugadaActual)) {
//                idJugadores.add(id);
//            }
//        }
//        
//        for(int id : aux){
//            System.out.println(jugadas.get(id).getCartas());
//        }
//        
//        System.out.println("Ganadores: ");
//        
//        for(int id : idJugadores){
//            System.out.println("Jugador " + id);
//        }

    }
}
