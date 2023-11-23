/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package poker3;

/**
 *
 * @author shangyu
 */
import controller.Controller;
import gui.MainFrame;
import model.*;

public class Poker3 {

    public static void main(String[] args) {
        Logica logica=new Logica();
        MainFrame frame = new MainFrame();
        Controller controller = new Controller(logica,frame);
        logica.setController(controller);
        frame.setController(controller);
        frame.setVisible(true);
    }
}
