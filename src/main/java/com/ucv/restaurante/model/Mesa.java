package com.ucv.restaurante.model;

public class Mesa extends Seleccionable {
    private int idMesa;
    private int numero;
    private int capacidad;
    private String estado;

    public Mesa() {
    }

    public Mesa(int idMesa, int numero, int capacidad, String estado) {
        this.idMesa = idMesa;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
