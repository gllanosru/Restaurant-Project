package com.ucv.restaurante.model;

public class Mesero {
    private final String nombre;
    private final String codigo;

    public Mesero(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public String getNombre() { return nombre; }

    // Cuando intente mostrar al mesero, pinte directamente el Nombre y codigo.
    @Override
    public String toString() { return nombre + " (" + codigo + ")"; }
}