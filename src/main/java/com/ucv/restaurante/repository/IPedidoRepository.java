package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.Pedido;
import java.util.List;

public interface IPedidoRepository {
    List<Pedido> findAll();
    List<Pedido> findByFilters(String filtro);
    void save(Pedido pedido);
    int saveAndReturnId(Pedido pedido);
    void update(Pedido pedido);
    void delete(int idPedido);
    void deleteAll(List<Integer> ids);
}
