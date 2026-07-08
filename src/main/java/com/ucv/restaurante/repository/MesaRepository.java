package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Mesa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaRepository implements IMesaRepository {
    private final DatabaseConfig dbConfig;

    public MesaRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<Mesa> findAll() { return findByFilters(""); }

    @Override
    public List<Mesa> findByFilters(String filtro) {
        String sql = "SELECT IdMesa, Numero, Capacidad, Estado FROM Mesa WHERE Estado LIKE ? OR CAST(Numero AS varchar) LIKE ? ORDER BY Numero";
        List<Mesa> mesas = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) mesas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar mesas: " + e.getMessage(), e);
        }
        return mesas;
    }

    @Override
    public void save(Mesa m) {
        String sql = "INSERT INTO Mesa (Numero, Capacidad, Estado) VALUES (?, ?, ?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getNumero());
            ps.setInt(2, m.getCapacidad());
            ps.setString(3, nvl(m.getEstado()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar mesa: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Mesa m) {
        String sql = "UPDATE Mesa SET Numero=?, Capacidad=?, Estado=? WHERE IdMesa=?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, m.getNumero());
            ps.setInt(2, m.getCapacidad());
            ps.setString(3, nvl(m.getEstado()));
            ps.setInt(4, m.getIdMesa());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar mesa: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idMesa) {
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM Mesa WHERE IdMesa=?")) {
            ps.setInt(1, idMesa);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mesa: " + e.getMessage(), e);
        }
    }

    @Override public void deleteAll(List<Integer> ids) { deleteAllByIds(ids); }

    private Mesa mapRow(ResultSet rs) throws SQLException {
        return new Mesa(rs.getInt("IdMesa"), rs.getInt("Numero"), rs.getInt("Capacidad"), rs.getString("Estado"));
    }

    private String like(String value) { return "%" + nvl(value) + "%"; }
    private String nvl(String value) { return value == null ? "" : value.trim(); }

    private void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM Mesa WHERE IdMesa IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mesas seleccionadas: " + e.getMessage(), e);
        }
    }
}
