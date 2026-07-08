package com.ucv.restaurante.model;

public class Mesero extends Seleccionable {
    private int idMesero;
    private String nombres;
    private String dni;
    private String telefono;
    private boolean activo;

    public Mesero() {
    }

    public Mesero(int idMesero, String nombres, String dni, String telefono, boolean activo) {
        this.idMesero = idMesero;
        this.nombres = nombres;
        this.dni = dni;
        this.telefono = telefono;
        this.activo = activo;
    }

    public int getIdMesero() { return idMesero; }
    public void setIdMesero(int idMesero) { this.idMesero = idMesero; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
