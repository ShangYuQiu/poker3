package poker3;

import controller.Controller;
import gui.MainFrame;
import model.*;

public class Poker3 {

    public static void main(String[] args) {
        Logic logica = new Logic();
        MainFrame frame = new MainFrame();
        Controller controller = new Controller(logica, frame);
        logica.setController(controller);
        frame.setController(controller);
        frame.setVisible(true);

    }
}
