
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author shangyu
 */
public class Logica {
    
    private static String simb[] = {"A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static String palos[] = {"h", "d", "s", "c"};
    
    private List<Carta> board;
    private List<Carta> allCarta;
    private List<Jugador> jug;
    
    public Logica (){
    
        board = new SortedArrayList<>();
        jug = new ArrayList <>(6);
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
            allCarta.removeAll(jug.get(i).getCartas());
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
    
}
