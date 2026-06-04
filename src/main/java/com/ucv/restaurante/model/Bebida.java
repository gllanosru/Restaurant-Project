package com.ucv.restaurante.model;

public class Bebida extends Producto {
    private String tamaño;

    public Bebida(String nombre, double precio, String tamaño) {
        super(nombre, precio);  //Invoca al constuctor de la Clase Padre. Inicializando..
        this.tamaño = tamaño;
    }

    //Sobreescribe el metodo abstracto de Producto..
    @Override
    public String getDetalles() { return nombre + " [" + tamaño + "]"; }
}