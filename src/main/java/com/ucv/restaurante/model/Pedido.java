package com.ucv.restaurante.model;

import java.time.LocalDateTime;

public class Pedido extends Seleccionable {
    private int idPedido;
    private int numeroMesa;
    private String nombreMesero;
    private String estado;
    private double total;
    private LocalDateTime fecha;

    public Pedido() {
    }

    public Pedido(int idPedido, int numeroMesa, String nombreMesero, String estado, double total, LocalDateTime fecha) {
        this.idPedido = idPedido;
        this.numeroMesa = numeroMesa;
        this.nombreMesero = nombreMesero;
        this.estado = estado;
        this.total = total;
        this.fecha = fecha;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }
    public String getNombreMesero() { return nombreMesero; }
    public void setNombreMesero(String nombreMesero) { this.nombreMesero = nombreMesero; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
