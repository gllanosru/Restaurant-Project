package com.ucv.restaurante.model;
import java.util.ArrayList;
import java.util.List;

public class GirasolRestaurante {
    private static GirasolRestaurante instancia;
    private final List<Producto> menu = new ArrayList<>();
    private final List<Pedido> listaPedidos = new ArrayList<>();

    private GirasolRestaurante() { cargarMenu(); }

    public static synchronized GirasolRestaurante getInstancia() {
        if (instancia == null) instancia = new GirasolRestaurante();
        return instancia;
    }

    private void cargarMenu() {
        menu.add(new Plato("Lomo Saltado", 32.0, "Personal"));
        menu.add(new Plato("Ceviche Clásico", 35.0, "Personal"));
        menu.add(new Plato("Arroz con Pollo", 25.0, "Familiar"));
        menu.add(new Plato("Causa Rellena", 18.0, "Personal"));
        menu.add(new Bebida("Chicha Morada", 12.0, "Jarra"));
        menu.add(new Bebida("Inca Kola", 3.5, "Personal"));
        menu.add(new Bebida("Coca Cola", 3.5, "Personal"));
    }

    public List<Producto> getMenu() { return menu; }
    public List<Pedido> getListaPedidos() { return listaPedidos; }
}