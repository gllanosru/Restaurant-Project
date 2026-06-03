package com.ucv.restaurante.model;
public class Mesa {
    private final int numero;
    private final int capacidad;
    private boolean ocupada;

    public Mesa(int numero, int capacidad) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ocupada = false;
    }

    public int getNumero()       { return numero; }
    public int getCapacidad()    { return capacidad; }
    public boolean isOcupada()   { return ocupada; }
    public void ocupar()         { this.ocupada = true; }
    public void liberar()        { this.ocupada = false; }
}