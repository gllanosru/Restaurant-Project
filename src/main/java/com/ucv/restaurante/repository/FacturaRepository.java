package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaRepository implements IFacturaRepository {
    private final DatabaseConfig dbConfig;

    public FacturaRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<Factura> findAll() { return findByFilters(""); }

    @Override
    public List<Factura> findByFilters(String filtro) {
        String sql = "SELECT IdFactura, IdPedido, Cliente, RUC, Total, Estado, Fecha FROM Factura WHERE Cliente LIKE ? OR RUC LIKE ? OR Estado LIKE ? ORDER BY Fecha DESC";
        List<Factura> facturas = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            ps.setString(3, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) facturas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar facturas: " + e.getMessage(), e);
        }
        return facturas;
    }

    @Override
    public void save(Factura f) {
        String sql = "INSERT INTO Factura (IdPedido, Cliente, RUC, Total, Estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getIdPedido());
            ps.setString(2, nvl(f.getCliente()));
            ps.setString(3, nvl(f.getRuc()));
            ps.setDouble(4, f.getTotal());
            ps.setString(5, nvl(f.getEstado()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar factura: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Factura f) {
        String sql = "UPDATE Factura SET Cliente=?, RUC=?, Total=?, Estado=? WHERE IdFactura=?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(f.getCliente()));
            ps.setString(2, nvl(f.getRuc()));
            ps.setDouble(3, f.getTotal());
            ps.setString(4, nvl(f.getEstado()));
            ps.setInt(5, f.getIdFactura());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar factura: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idFactura) {
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM Factura WHERE IdFactura=?")) {
            ps.setInt(1, idFactura);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar factura: " + e.getMessage(), e);
        }
    }

    @Override public void deleteAll(List<Integer> ids) { deleteAllByIds(ids); }

    private Factura mapRow(ResultSet rs) throws SQLException {
        return new Factura(rs.getInt("IdFactura"), rs.getInt("IdPedido"), rs.getString("Cliente"),
                rs.getString("RUC"), rs.getDouble("Total"), rs.getString("Estado"),
                rs.getTimestamp("Fecha").toLocalDateTime());
    }

    private String like(String value) { return "%" + nvl(value) + "%"; }
    private String nvl(String value) { return value == null ? "" : value.trim(); }

    private void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM Factura WHERE IdFactura IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar facturas seleccionadas: " + e.getMessage(), e);
        }
    }
}
