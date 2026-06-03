package com.ucv.restaurante.model;

public class Mesero {
    private final String nombre;
    private final String codigo;
    private final int turno;

    public Mesero(String nombre, String codigo, int turno) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.turno = turno;
    }

    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getTurno()     { return turno; }

    @Override
    public String toString() { return nombre + " (" + codigo + ")"; }
}