package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.Mesa;
import java.util.List;

public interface IMesaRepository {
    List<Mesa> findAll();
    List<Mesa> findByFilters(String filtro);
    void save(Mesa mesa);
    void update(Mesa mesa);
    void delete(int idMesa);
    void deleteAll(List<Integer> ids);
}
