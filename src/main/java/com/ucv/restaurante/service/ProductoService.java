package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Producto;
import com.ucv.restaurante.repository.IProductoRepository;

import java.util.List;

public class ProductoService implements IProductoService {
    private final IProductoRepository repository;

    public ProductoService(IProductoRepository repository) {
        this.repository = repository;
    }

    @Override public List<Producto> listar() { return repository.findAll(); }
    @Override public List<Producto> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public void crear(Producto producto) { validar(producto); repository.save(producto); }
    @Override public void actualizar(Producto producto) { validar(producto); repository.update(producto); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) throw new IllegalArgumentException("El producto es obligatorio.");
        if (p.getCategoria() == null || p.getCategoria().trim().isEmpty()) throw new IllegalArgumentException("La categoria es obligatoria.");
        if (p.getPrecio() <= 0) throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        if (p.getStock() < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
    }
}
