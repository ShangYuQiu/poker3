/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.List;

/**
 *
 * @author shangyu
 */
public class Jugador {
    
    private List<Carta> cartas;
    private int id;
    private double puntos;
    
    public Jugador (List<Carta> c, int id){
        this.cartas = c;
        this.id = id;
        puntos = 0;
    }
    
    public List<Carta>getCartas(){
        return cartas;
    }
    
    public double getPuntos (){
        return puntos;
    }
}
