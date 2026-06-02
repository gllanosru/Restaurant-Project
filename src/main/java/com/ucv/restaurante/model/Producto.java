package com.ucv.restaurante.model;  //Solemos utilizar como un identificador que le dice a java en que carpeta esta.
import javafx.beans.property.SimpleIntegerProperty;

public abstract class Producto { //Abstract porque no existe un producto generico.
    protected String nombre;
    protected double precio;
    // La propiedad se queda aquí para no necesitar otra clase contenedora
    private final SimpleIntegerProperty cantTemp = new SimpleIntegerProperty(0);

    public Producto(String nombre, double precio) { this.nombre = nombre; this.precio = precio; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    // Métodos para que el TableView de JavaFX lea directo el texto y precio
    public String getNombreDetallado() { return nombre; }
    public double getPrecioUnidad() { return precio; }
    public int getCantTemp() { return cantTemp.get(); }
    public SimpleIntegerProperty cantTempProperty() { return cantTemp; }

    public void cambiarCant(int valor) {
        int nueva = cantTemp.get() + valor;
        if (nueva >= 0) cantTemp.set(nueva);
    }
    public abstract String getDetalles();
}