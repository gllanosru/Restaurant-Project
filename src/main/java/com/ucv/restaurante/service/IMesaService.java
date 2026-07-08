package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Mesa;
import java.util.List;

public interface IMesaService {
    List<Mesa> listar();
    List<Mesa> buscar(String filtro);
    void crear(Mesa mesa);
    void actualizar(Mesa mesa);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Mesa mesa);
}
