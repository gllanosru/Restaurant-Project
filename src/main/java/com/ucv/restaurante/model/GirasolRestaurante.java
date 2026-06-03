package com.ucv.restaurante.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GirasolRestaurante {
    private static GirasolRestaurante instancia;
    private final List<Producto> menu          = new ArrayList<>();
    private final List<Pedido>   listaPedidos  = new ArrayList<>();
    private final List<Mesa>     listaMesas    = new ArrayList<>();
    private final List<Mesero>   listaMeseros  = new ArrayList<>();

    private GirasolRestaurante() {
        cargarMenu();
        cargarMesas();
        cargarMeseros();
    }
    public static synchronized GirasolRestaurante getInstancia() {
        if (instancia == null) instancia = new GirasolRestaurante();
        return instancia;
    }
    private void cargarMenu() {
        menu.add(new Plato("Lomo Saltado",   32.0, "Personal"));
        menu.add(new Plato("Ceviche Clásico",35.0, "Personal"));
        menu.add(new Plato("Arroz con Pollo",25.0, "Familiar"));
        menu.add(new Plato("Causa Rellena",  18.0, "Personal"));
        menu.add(new Bebida("Chicha Morada", 12.0, "Jarra"));
        menu.add(new Bebida("Inca Kola",      3.5, "Personal"));
        menu.add(new Bebida("Coca Cola",      3.5, "Personal"));
    }
    private void cargarMesas() {
        for (int i = 1; i <= 10; i++) {
            listaMesas.add(new Mesa(i, 4));
        }
    }
    private void cargarMeseros() {
        listaMeseros.add(new Mesero("Carlos López", "M01", 1));
        listaMeseros.add(new Mesero("Ana García",   "M02", 1));
        listaMeseros.add(new Mesero("Pedro Ruiz",   "M03", 2));
    }

    public List<Producto> getMenu()            { return menu; }
    public List<Pedido>   getListaPedidos()    { return listaPedidos; }
    public List<Mesa>     getListaMesas()      { return listaMesas; }
    public List<Mesero>   getListaMeseros()    { return listaMeseros; }

    public Mesa getMesa(int numero) {
        return listaMesas.stream()
                .filter(m -> m.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public Factura generarFactura(int mesa) {
        List<Pedido> delaMesa = listaPedidos.stream()
                .filter(p -> p.getNumeroMesa() == mesa)
                .collect(Collectors.toList());
        return new Factura(mesa, delaMesa);
    }
}