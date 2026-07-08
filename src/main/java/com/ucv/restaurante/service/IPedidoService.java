package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Pedido;
import java.util.List;

public interface IPedidoService {
    List<Pedido> listar();
    List<Pedido> buscar(String filtro);
    void crear(Pedido pedido);
    int crearRetornandoId(Pedido pedido);
    void actualizar(Pedido pedido);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(Pedido pedido);
}
