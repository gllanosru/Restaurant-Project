package com.ucv.restaurante.model;

public class Bebida extends Producto {
    private String tamaño;

    public Bebida(String nombre, double precio, String tamaño) {
        super(nombre, precio);
        this.tamaño = tamaño;
    }

    @Override
    public String getDetalles() { return nombre + " [" + tamaño + "]"; }
}