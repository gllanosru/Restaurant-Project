package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.Factura;
import java.util.List;

public interface IFacturaRepository {
    List<Factura> findAll();
    List<Factura> findByFilters(String filtro);
    void save(Factura factura);
    void update(Factura factura);
    void delete(int idFactura);
    void deleteAll(List<Integer> ids);
}
