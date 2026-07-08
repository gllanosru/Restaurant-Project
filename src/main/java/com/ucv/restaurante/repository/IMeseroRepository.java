package com.ucv.restaurante.repository;

import com.ucv.restaurante.model.Mesero;
import java.util.List;

public interface IMeseroRepository {
    List<Mesero> findAll();
    List<Mesero> findByFilters(String filtro);
    void save(Mesero mesero);
    void update(Mesero mesero);
    void delete(int idMesero);
    void deleteAll(List<Integer> ids);
}
