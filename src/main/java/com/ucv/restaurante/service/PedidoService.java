package com.ucv.restaurante.service;

import com.ucv.restaurante.model.Pedido;
import com.ucv.restaurante.repository.IPedidoRepository;

import java.util.List;

public class PedidoService implements IPedidoService {
    private final IPedidoRepository repository;

    public PedidoService(IPedidoRepository repository) {
        this.repository = repository;
    }

    @Override public List<Pedido> listar() { return repository.findAll(); }
    @Override public List<Pedido> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public void crear(Pedido pedido) { validar(pedido); repository.save(pedido); }
    @Override public int crearRetornandoId(Pedido pedido) { validar(pedido); return repository.saveAndReturnId(pedido); }
    @Override public void actualizar(Pedido pedido) { validar(pedido); repository.update(pedido); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(Pedido p) {
        if (p.getNumeroMesa() <= 0) throw new IllegalArgumentException("Seleccione una mesa valida.");
        if (p.getNombreMesero() == null || p.getNombreMesero().trim().isEmpty()) throw new IllegalArgumentException("Indique el mesero.");
        if (p.getEstado() == null || p.getEstado().trim().isEmpty()) throw new IllegalArgumentException("El estado es obligatorio.");
        if (p.getTotal() < 0) throw new IllegalArgumentException("El total no puede ser negativo.");
    }
}
