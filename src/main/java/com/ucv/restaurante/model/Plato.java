package com.ucv.restaurante.model;

public class Plato extends Producto {
    private String porcion;
    public Plato(String n, double p, String porc) { super(n, p); this.porcion = porc; }
    @Override
        public String getDetalles() { return "Plato: " + nombre + " (" + porcion + ") - S/ " + precio; }
}