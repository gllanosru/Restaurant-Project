package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.Producto;
import java.util.List;

public interface IProductoRepository {
    List<Producto> findAll();
    List<Producto> findByFilters(String filtro);
    void save(Producto producto);
    void update(Producto producto);
    void delete(int idProducto);
    void deleteAll(List<Integer> ids);
}
