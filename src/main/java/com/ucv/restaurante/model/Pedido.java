package com.ucv.restaurante.model;

public class Pedido {
    private final int numeroMesa;
    private final Producto producto;
    //private final int litros;
    private final int cantidad;

    public Pedido(int numeroMesa, Producto producto, int cantidad) {
        this.numeroMesa = numeroMesa;
        this.producto = producto;
       // this.litros = litros;
        this.cantidad = cantidad;
    }
    public int getNumeroMesa() { return numeroMesa; }
    public int getCantidad() { return cantidad; }
    public String getNombreProducto() { return producto.getNombre(); }
    //public int getLitros(){ return litros; }
    public double getSubtotal() { return producto.getPrecio() * cantidad; }
}