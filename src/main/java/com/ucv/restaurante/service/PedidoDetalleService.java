package com.ucv.restaurante.service;

import com.ucv.restaurante.model.PedidoDetalle;
import com.ucv.restaurante.repository.IPedidoDetalleRepository;

import java.util.List;

public class PedidoDetalleService implements IPedidoDetalleService {
    private final IPedidoDetalleRepository repository;

    public PedidoDetalleService(IPedidoDetalleRepository repository) {
        this.repository = repository;
    }

    @Override public List<PedidoDetalle> listar() { return repository.findAll(); }
    @Override public List<PedidoDetalle> buscar(String filtro) { return repository.findByFilters(filtro); }
    @Override public List<PedidoDetalle> buscarPorPedido(int idPedido) { return repository.findByPedido(idPedido); }
    @Override public void crear(PedidoDetalle detalle) { validar(detalle); repository.save(detalle); }
    @Override public void actualizar(PedidoDetalle detalle) { validar(detalle); repository.update(detalle); }
    @Override public void eliminar(int id) { repository.delete(id); }
    @Override public void reemplazarPorPedido(int idPedido, List<PedidoDetalle> detalles) {
        if (idPedido <= 0) throw new IllegalArgumentException("El pedido es obligatorio.");
        for (PedidoDetalle detalle : detalles) {
            detalle.setIdPedido(idPedido);
            validar(detalle);
        }
        repository.replaceByPedido(idPedido, detalles);
    }
    @Override public void eliminarSeleccionados(List<Integer> ids) { repository.deleteAll(ids); }

    @Override
    public void validar(PedidoDetalle d) {
        if (d.getIdPedido() <= 0) throw new IllegalArgumentException("El pedido es obligatorio.");
        if (d.getIdProducto() <= 0) throw new IllegalArgumentException("El producto es obligatorio.");
        if (d.getCantidad() <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        if (d.getPrecioUnitario() < 0) throw new IllegalArgumentException("El precio unitario no puede ser negativo.");
    }
}
