package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Factura;
import com.ucv.restaurante.repository.IFacturaRepository;

import java.util.List;

public class FacturaService implements IFacturaService {
    private final IFacturaRepository repository;

    public FacturaService(IFacturaRepository repository) {
        this.repository = repository;
    }

    @Override public List<Factura> listar() { return repository.findAll(); }
    @Override public List<Factura> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public void crear(Factura factura) { validar(factura); repository.save(factura); }
    @Override public void actualizar(Factura factura) { validar(factura); repository.update(factura); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Factura f) {
        if (f.getIdPedido() <= 0) throw new IllegalArgumentException("El pedido asociado es obligatorio.");
        if (f.getCliente() == null || f.getCliente().trim().isEmpty()) throw new IllegalArgumentException("El cliente es obligatorio.");
        if (f.getTotal() < 0) throw new IllegalArgumentException("El total no puede ser negativo.");
        if (f.getEstado() == null || f.getEstado().trim().isEmpty()) throw new IllegalArgumentException("El estado es obligatorio.");
    }
}
