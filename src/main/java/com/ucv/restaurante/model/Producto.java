package com.ucv.restaurante.model;

public abstract class Producto {
    protected String nombre;
    protected double precio;
    private int cantTemp = 0; // Simplificado: un entero simple en lugar de SimpleIntegerProperty

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getCantTemp() { return cantTemp; }

    public void cambiarCant(int valor) {
        int nueva = cantTemp + valor;
        if (nueva >= 0) cantTemp = nueva;
    }

    public abstract String getDetalles();
}