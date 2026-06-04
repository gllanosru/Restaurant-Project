package com.ucv.restaurante.model;
import java.util.ArrayList;
import java.util.List;

public class GirasolRestaurante {
    private static GirasolRestaurante instancia; // La única instancia

    private final List<Producto> menu = new ArrayList<>();
    private final List<Pedido> listaPedidos = new ArrayList<>();
    private final List<Mesero> listaMeseros = new ArrayList<>();

    // Constructor PRIVADO para que nadie use "new GirasolRestaurante()"
    private GirasolRestaurante() {
        cargarDatos();
    }
    //Metodo para obtener una unica instancia desde cualquier parte del codigo..
    public static synchronized GirasolRestaurante getInstancia() {
        if (instancia == null) instancia = new GirasolRestaurante();
        return instancia;
    }

    private void cargarDatos() {
        menu.add(new Plato("Lomo Saltado", 32.0, "Personal"));
        menu.add(new Plato("Ceviche Clásico", 35.0, "Personal"));
        menu.add(new Bebida("Chicha Morada", 12.0, "Jarra"));
        menu.add(new Bebida("Inca Kola", 3.5, "Personal"));

        listaMeseros.add(new Mesero("Diego Vela", "M01"));
        listaMeseros.add(new Mesero("Jack Pastor", "M02"));
    }

    public List<Producto> getMenu() { return menu; }
    public List<Pedido> getListaPedidos() { return listaPedidos; }
    public List<Mesero> getListaMeseros() { return listaMeseros; }
}