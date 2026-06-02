package com.ucv.restaurante.model;

public class Bebida extends Producto {
    private String tamaño;
    public Bebida(String n, double p, String t) { super(n, p); this.tamaño = t; }
    @Override
        public String getDetalles() { return "Bebida: " + nombre + " [" + tamaño + "] - S/ " + precio; }
}