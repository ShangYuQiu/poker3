package poker3;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class Poker3 {

    public static void main(String[] args) {
        Logic logica = new Logic();
//        MainFrame frame = new MainFrame();
//        Controller controller = new Controller(logica, frame);
//        logica.setController(controller);
//        frame.setController(controller);
//        frame.setVisible(true);
        List<Carta> cartas = new ArrayList<>();
        cartas.add(new Carta("6", "h"));
        cartas.add(new Carta("A", "d"));
        cartas.add(new Carta("J", "S"));
        cartas.add(new Carta("K", "h"));
        cartas.add(new Carta("Q", "C"));
        
        List<Carta> combinacionActual = new ArrayList<>();
        logica.generarCombinaciones(cartas, 2, combinacionActual, 0);
    }
}
