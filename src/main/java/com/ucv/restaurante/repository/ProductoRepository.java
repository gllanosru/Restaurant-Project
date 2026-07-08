package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository implements IProductoRepository {
    private final DatabaseConfig dbConfig;

    public ProductoRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<Producto> findAll() { return findByFilters(""); }

    @Override
    public List<Producto> findByFilters(String filtro) {
        String sql = """
                SELECT IdProducto, Nombre, Categoria, Precio, Stock, Activo
                FROM Producto
                WHERE Nombre LIKE ? OR Categoria LIKE ?
                ORDER BY Nombre
                """;
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) productos.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar productos: " + e.getMessage(), e);
        }
        return productos;
    }

    @Override
    public void save(Producto p) {
        String sql = "INSERT INTO Producto (Nombre, Categoria, Precio, Stock, Activo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(p.getNombre()));
            ps.setString(2, nvl(p.getCategoria()));
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setBoolean(5, p.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Producto p) {
        String sql = "UPDATE Producto SET Nombre=?, Categoria=?, Precio=?, Stock=?, Activo=? WHERE IdProducto=?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(p.getNombre()));
            ps.setString(2, nvl(p.getCategoria()));
            ps.setDouble(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setBoolean(5, p.isActivo());
            ps.setInt(6, p.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idProducto) {
        try (Connection conn = dbConfig.getConnection()) {
            if (estaUsadoEnPedido(conn, idProducto)) {
                throw new IllegalStateException("No se puede eliminar el producto porque ya forma parte de uno o mas pedidos. Para no afectar el historial, edite el producto y marquelo como inactivo.");
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Producto WHERE IdProducto=?")) {
                ps.setInt(1, idProducto);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAll(List<Integer> ids) {
        deleteAllByIds("Producto", "IdProducto", ids);
    }

    private Producto mapRow(ResultSet rs) throws SQLException {
        return new Producto(rs.getInt("IdProducto"), rs.getString("Nombre"), rs.getString("Categoria"),
                rs.getDouble("Precio"), rs.getInt("Stock"), rs.getBoolean("Activo"));
    }

    private String like(String value) { return "%" + nvl(value) + "%"; }
    private String nvl(String value) { return value == null ? "" : value.trim(); }

    private boolean estaUsadoEnPedido(Connection conn, int idProducto) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PedidoDetalle WHERE IdProducto=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private void deleteAllByIds(String table, String idColumn, List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(table).append(" WHERE ").append(idColumn).append(" IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar productos seleccionados: " + e.getMessage(), e);
        }
    }
}
