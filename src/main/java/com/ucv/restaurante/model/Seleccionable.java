package com.ucv.restaurante.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public abstract class Seleccionable {
    private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

    public boolean isSeleccionado() {
        return seleccionado.get();
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado.set(seleccionado);
    }

    public BooleanProperty seleccionadoProperty() {
        return seleccionado;
    }
}
