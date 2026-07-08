package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Mesa;
import com.ucv.restaurante.repository.IMesaRepository;

import java.util.List;

public class MesaService implements IMesaService {
    private final IMesaRepository repository;

    public MesaService(IMesaRepository repository) {
        this.repository = repository;
    }

    @Override public List<Mesa> listar() { return repository.findAll(); }
    @Override public List<Mesa> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public void crear(Mesa mesa) { validar(mesa); repository.save(mesa); }
    @Override public void actualizar(Mesa mesa) { validar(mesa); repository.update(mesa); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Mesa m) {
        if (m.getNumero() <= 0) throw new IllegalArgumentException("El numero de mesa debe ser mayor a cero.");
        if (m.getCapacidad() <= 0) throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        if (m.getEstado() == null || m.getEstado().trim().isEmpty()) throw new IllegalArgumentException("El estado es obligatorio.");
    }
}
