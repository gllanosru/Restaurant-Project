package com.ucv.restaurante.model;
import java.util.List;

public class Factura {
    private final int numeroMesa;
    private final List<Pedido> pedidos;

    public Factura(int numeroMesa, List<Pedido> pedidos) {
        this.numeroMesa = numeroMesa;
        this.pedidos = pedidos;
    }

    public int getNumeroMesa()  { return numeroMesa; }
    public List<Pedido> getPedidos() { return pedidos; }

    public double calcularTotal() {
        return pedidos.stream().mapToDouble(Pedido::getSubtotal).sum();
    }

    public String getResumen() {
        return String.format("Mesa %d | %d ítem(s) | Total: S/ %.2f",
                numeroMesa, pedidos.size(), calcularTotal());
    }
}