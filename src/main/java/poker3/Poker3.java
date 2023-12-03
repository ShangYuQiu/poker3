package poker3;

import model.Logic;
import controller.Controller;
import gui.MainFrame;
public class Poker3 {

    public static void main(String[] args) {
        Logic logic = new Logic();
        MainFrame frame = new MainFrame();
        Controller controller = new Controller(logic, frame);
        logic.setController(controller);
        frame.setController(controller);
        frame.setVisible(true);

    }
}
