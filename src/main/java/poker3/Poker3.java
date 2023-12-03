package poker3;

import java.util.List;
import model.Carta;
import model.Logic;
import model.SortedArrayList;

public class Poker3 {

    public static void main(String[] args) {
        Logic logic = new Logic();
//        MainFrame frame = new MainFrame();
//        Controller controller = new Controller(logic, frame);
//        logic.setController(controller);
//        frame.setController(controller);
//        frame.setVisible(true);

        List<Carta> lista = new SortedArrayList<>();
        lista.add(new Carta("A", "d"));
        lista.add(new Carta("2", "d"));
        lista.add(new Carta("J", "h"));
        lista.add(new Carta("5", "h"));
        lista.add(new Carta("7", "c"));
        lista.add(new Carta("2", "s"));
        lista.add(new Carta("K", "d"));
        lista.add(new Carta("A", "s"));

        System.out.println(lista);

        System.out.println(logic.Pareja(lista).getCartas());

    }
}
