package com.ucv.restaurante.service;

import com.ucv.restaurante.model.PedidoDetalle;
import java.util.List;

public interface IPedidoDetalleService {
    List<PedidoDetalle> listar();
    List<PedidoDetalle> buscar(String filtro);
    List<PedidoDetalle> buscarPorPedido(int idPedido);
    void crear(PedidoDetalle detalle);
    void actualizar(PedidoDetalle detalle);
    void eliminar(int id);
    void reemplazarPorPedido(int idPedido, List<PedidoDetalle> detalles);
    void eliminarSeleccionados(List<Integer> ids);
    void validar(PedidoDetalle detalle);
}
