package com.ucv.restaurante.model;

import java.time.LocalDateTime;

public class Factura extends Seleccionable {
    private int idFactura;
    private int idPedido;
    private String cliente;
    private String ruc;
    private double total;
    private String estado;
    private LocalDateTime fecha;

    public Factura() {
    }

    public Factura(int idFactura, int idPedido, String cliente, String ruc, double total, String estado, LocalDateTime fecha) {
        this.idFactura = idFactura;
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.ruc = ruc;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
