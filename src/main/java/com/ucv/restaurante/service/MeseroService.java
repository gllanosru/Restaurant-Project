package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Mesero;
import com.ucv.restaurante.repository.IMeseroRepository;

import java.util.List;

public class MeseroService implements IMeseroService {
    private final IMeseroRepository repository;

    public MeseroService(IMeseroRepository repository) {
        this.repository = repository;
    }

    @Override public List<Mesero> listar() { return repository.findAll(); }
    @Override public List<Mesero> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public void crear(Mesero mesero) { validar(mesero); repository.save(mesero); }
    @Override public void actualizar(Mesero mesero) { validar(mesero); repository.update(mesero); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Mesero m) {
        if (m.getNombres() == null || m.getNombres().trim().isEmpty()) throw new IllegalArgumentException("El nombre del mesero es obligatorio.");
        if (m.getDni() == null || !m.getDni().matches("\\d{8}")) throw new IllegalArgumentException("El DNI debe tener 8 digitos.");
    }
}
