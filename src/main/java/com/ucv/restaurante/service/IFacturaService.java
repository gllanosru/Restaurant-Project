package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Factura;
import java.util.List;

public interface IFacturaService {
    List<Factura> listar();
    List<Factura> buscar(String filtro);
    void crear(Factura factura);
    void actualizar(Factura factura);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Factura factura);
}
