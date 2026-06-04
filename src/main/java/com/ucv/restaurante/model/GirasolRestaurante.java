package com.ucv.restaurante.model;
import java.util.ArrayList;
import java.util.List;


public class GirasolRestaurante {
    private static GirasolRestaurante instancia;

    private final List<Producto> menu = new ArrayList<>();
    private final List<Pedido> listaPedidos = new ArrayList<>();
    private final List<Mesero> listaMeseros = new ArrayList<>();

    // Constructor PRIVADO para que nadie use "new GirasolRestaurante()"
    private GirasolRestaurante() { // El constructor es privado. Esto impide que otras puedan hacer new.
        cargarDatos();
    }
    //Metodo para obtener una unica instancia desde cualquier parte del codigo..
    public static synchronized GirasolRestaurante getInstancia() { // synchronized evita problemas si dos hilos estan creando al mismo tiempo
        if (instancia == null) instancia = new GirasolRestaurante();
        return instancia;
    }

    private void cargarDatos() {

        //Platos disponibles
        menu.add(new Plato("Lomo Saltado", 32.0, "Personal"));
        menu.add(new Plato("Ceviche Clásico", 35.0, "Personal"));
        menu.add(new Plato("Arroz con Pato", 25.0, "Personal"));
        menu.add(new Plato("Anticucho", 12.0, "Personal"));
        menu.add(new Plato("Leche de tigre", 15.0, "Personal"));

        //Bebidas disponibles
        menu.add(new Bebida("Coca Cola", 3.50, "Personal"));
        menu.add(new Bebida("Chicha Morada", 12.0, "Jarra"));
        menu.add(new Bebida("Inca Kola", 3.50, "Personal"));

        //Lista de Meseros disponibles
        listaMeseros.add(new Mesero("Diego Vela", "M01"));
        listaMeseros.add(new Mesero("Jack Pastor", "M02"));
        listaMeseros.add(new Mesero("Mayerly Venezuela", "M03"));
        listaMeseros.add(new Mesero("Gian Carlos", "M04"));
    }

    public List<Producto> getMenu() { return menu; }
    public List<Pedido> getListaPedidos() { return listaPedidos; }
    public List<Mesero> getListaMeseros() { return listaMeseros; }
}