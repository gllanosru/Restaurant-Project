package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.PedidoDetalle;
import java.util.List;

public interface IPedidoDetalleRepository {
    List<PedidoDetalle> findAll();
    List<PedidoDetalle> findByFilters(String filtro);
    List<PedidoDetalle> findByPedido(int idPedido);
    void save(PedidoDetalle detalle);
    void update(PedidoDetalle detalle);
    void delete(int idDetalle);
    void replaceByPedido(int idPedido, List<PedidoDetalle> detalles);
    void deleteAll(List<Integer> ids);
}
