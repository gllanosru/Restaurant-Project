package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.PedidoDetalle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDetalleRepository implements IPedidoDetalleRepository {
    private final DatabaseConfig dbConfig;

    public PedidoDetalleRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<PedidoDetalle> findAll() { return findByFilters(""); }

    @Override
    public List<PedidoDetalle> findByFilters(String filtro) {
        String sql = """
                SELECT d.IdDetalle, d.IdPedido, d.IdProducto, p.Nombre AS Producto,
                       d.Cantidad, d.PrecioUnitario, d.Subtotal
                FROM PedidoDetalle d
                INNER JOIN Producto p ON d.IdProducto = p.IdProducto
                WHERE CAST(d.IdPedido AS varchar) LIKE ?
                   OR p.Nombre LIKE ?
                   OR CAST(d.IdProducto AS varchar) LIKE ?
                ORDER BY d.IdPedido DESC, d.IdDetalle
                """;
        List<PedidoDetalle> detalles = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            ps.setString(3, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) detalles.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar detalles de pedido: " + e.getMessage(), e);
        }
        return detalles;
    }

    @Override
    public List<PedidoDetalle> findByPedido(int idPedido) {
        String sql = """
                SELECT d.IdDetalle, d.IdPedido, d.IdProducto, p.Nombre AS Producto,
                       d.Cantidad, d.PrecioUnitario, d.Subtotal
                FROM PedidoDetalle d
                INNER JOIN Producto p ON d.IdProducto = p.IdProducto
                WHERE d.IdPedido = ?
                ORDER BY d.IdDetalle
                """;
        List<PedidoDetalle> detalles = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) detalles.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar detalle del pedido: " + e.getMessage(), e);
        }
        return detalles;
    }

    @Override
    public void save(PedidoDetalle d) {
        String sql = "INSERT INTO PedidoDetalle (IdPedido, IdProducto, Cantidad, PrecioUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getIdPedido());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.executeUpdate();
            actualizarTotalPedido(conn, d.getIdPedido());
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar detalle de pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(PedidoDetalle d) {
        String sql = "UPDATE PedidoDetalle SET IdPedido=?, IdProducto=?, Cantidad=?, PrecioUnitario=? WHERE IdDetalle=?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int pedidoAnterior = buscarPedidoAnterior(conn, d.getIdDetalle());
            ps.setInt(1, d.getIdPedido());
            ps.setInt(2, d.getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getPrecioUnitario());
            ps.setInt(5, d.getIdDetalle());
            ps.executeUpdate();
            actualizarTotalPedido(conn, d.getIdPedido());
            if (pedidoAnterior > 0 && pedidoAnterior != d.getIdPedido()) actualizarTotalPedido(conn, pedidoAnterior);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar detalle de pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idDetalle) {
        try (Connection conn = dbConfig.getConnection()) {
            int idPedido = buscarPedidoAnterior(conn, idDetalle);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PedidoDetalle WHERE IdDetalle=?")) {
                ps.setInt(1, idDetalle);
                ps.executeUpdate();
            }
            if (idPedido > 0) actualizarTotalPedido(conn, idPedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar detalle de pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (Integer id : ids) delete(id);
    }

    @Override
    public void replaceByPedido(int idPedido, List<PedidoDetalle> detalles) {
        try (Connection conn = dbConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PedidoDetalle WHERE IdPedido=?")) {
                    ps.setInt(1, idPedido);
                    ps.executeUpdate();
                }
                String sql = "INSERT INTO PedidoDetalle (IdPedido, IdProducto, Cantidad, PrecioUnitario) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (PedidoDetalle d : detalles) {
                        ps.setInt(1, idPedido);
                        ps.setInt(2, d.getIdProducto());
                        ps.setInt(3, d.getCantidad());
                        ps.setDouble(4, d.getPrecioUnitario());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                actualizarTotalPedido(conn, idPedido);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar detalle del pedido: " + e.getMessage(), e);
        }
    }

    private PedidoDetalle mapRow(ResultSet rs) throws SQLException {
        return new PedidoDetalle(rs.getInt("IdDetalle"), rs.getInt("IdPedido"), rs.getInt("IdProducto"),
                rs.getString("Producto"), rs.getInt("Cantidad"), rs.getDouble("PrecioUnitario"),
                rs.getDouble("Subtotal"));
    }

    private int buscarPedidoAnterior(Connection conn, int idDetalle) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT IdPedido FROM PedidoDetalle WHERE IdDetalle=?")) {
            ps.setInt(1, idDetalle);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("IdPedido") : 0;
            }
        }
    }

    private void actualizarTotalPedido(Connection conn, int idPedido) throws SQLException {
        String sql = """
                UPDATE Pedido
                SET Total = ISNULL((SELECT SUM(Subtotal) FROM PedidoDetalle WHERE IdPedido = ?), 0)
                WHERE IdPedido = ?
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    private String like(String value) { return "%" + (value == null ? "" : value.trim()) + "%"; }
}
