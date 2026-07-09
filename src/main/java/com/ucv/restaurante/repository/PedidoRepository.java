package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository implements IPedidoRepository {
    private final DatabaseConfig dbConfig;

    public PedidoRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<Pedido> findAll() { return findByFilters(""); }


    @Override
    public List<Pedido> findByFilters(String filtro) {
        String sql = """
                SELECT p.IdPedido, m.Numero AS NumeroMesa, me.Nombres AS NombreMesero, p.Estado, p.Total, p.Fecha
                FROM Pedido p
                INNER JOIN Mesa m ON p.IdMesa = m.IdMesa
                INNER JOIN Mesero me ON p.IdMesero = me.IdMesero
                WHERE p.Estado LIKE ? OR me.Nombres LIKE ? OR CAST(m.Numero AS varchar) LIKE ?
                ORDER BY p.Fecha DESC
                """;
        List<Pedido> pedidos = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            ps.setString(3, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) pedidos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar pedidos: " + e.getMessage(), e);
        }
        return pedidos;
    }

    @Override
    public void save(Pedido p) {
        saveAndReturnId(p);
    }

    @Override
    public int saveAndReturnId(Pedido p) {
        String sql = """
                INSERT INTO Pedido (IdMesa, IdMesero, Estado, Total)
                OUTPUT INSERTED.IdPedido
                SELECT m.IdMesa, me.IdMesero, ?, ?
                FROM Mesa m CROSS JOIN Mesero me
                WHERE m.Numero = ? AND me.Nombres = ?
                """;
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(p.getEstado()));
            ps.setDouble(2, p.getTotal());
            ps.setInt(3, p.getNumeroMesa());
            ps.setString(4, nvl(p.getNombreMesero()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new SQLException("No se encontro mesa o mesero para registrar el pedido.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Pedido p) {
        String sql = """
                UPDATE Pedido
                SET IdMesa = (SELECT IdMesa FROM Mesa WHERE Numero = ?),
                    IdMesero = (SELECT IdMesero FROM Mesero WHERE Nombres = ?),
                    Estado = ?,
                    Total = ?
                WHERE IdPedido = ?
                """;
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getNumeroMesa());
            ps.setString(2, nvl(p.getNombreMesero()));
            ps.setString(3, nvl(p.getEstado()));
            ps.setDouble(4, p.getTotal());
            ps.setInt(5, p.getIdPedido());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar pedido: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idPedido) {
        try (Connection conn = dbConfig.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM PedidoDetalle WHERE IdPedido=?")) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Pedido WHERE IdPedido=?")) {
                ps.setInt(1, idPedido);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pedido: " + e.getMessage(), e);
        }
    }

    @Override public void deleteAll(List<Integer> ids) { deleteAllByIds(ids); }

    private Pedido mapRow(ResultSet rs) throws SQLException {
        return new Pedido(rs.getInt("IdPedido"), rs.getInt("NumeroMesa"), rs.getString("NombreMesero"),
                rs.getString("Estado"), rs.getDouble("Total"), rs.getTimestamp("Fecha").toLocalDateTime());
    }

    private String like(String value) { return "%" + nvl(value) + "%"; }
    private String nvl(String value) { return value == null ? "" : value.trim(); }

    private void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM Pedido WHERE IdPedido IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar pedidos seleccionados: " + e.getMessage(), e);
        }
    }
}
