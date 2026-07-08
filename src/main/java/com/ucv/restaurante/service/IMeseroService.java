package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Mesero;
import java.util.List;

public interface IMeseroService {
    List<Mesero> listar();
    List<Mesero> buscar(String filtro);
    void crear(Mesero mesero);
    void actualizar(Mesero mesero);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Mesero mesero);
}
