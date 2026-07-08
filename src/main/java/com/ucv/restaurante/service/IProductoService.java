package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Producto;
import java.util.List;

public interface IProductoService {
    List<Producto> listar();
    List<Producto> buscar(String filtro);
    void crear(Producto producto);
    void actualizar(Producto producto);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Producto producto);
}
