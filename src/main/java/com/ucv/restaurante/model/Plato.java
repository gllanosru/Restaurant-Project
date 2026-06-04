package com.ucv.restaurante.model;

public class Plato extends Producto {
    private String porcion;

    public Plato(String nombre, double precio, String porcion) {
        super(nombre, precio); // Llama al constructor del padre (Producto)
        this.porcion = porcion;
    }

    @Override
    public String getDetalles() { return nombre + " (" + porcion + ")"; }
}