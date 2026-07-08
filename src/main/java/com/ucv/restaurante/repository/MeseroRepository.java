package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Mesero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeseroRepository implements IMeseroRepository {
    private final DatabaseConfig dbConfig;

    public MeseroRepository(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override public List<Mesero> findAll() { return findByFilters(""); }

    @Override
    public List<Mesero> findByFilters(String filtro) {
        String sql = "SELECT IdMesero, Nombres, DNI, Telefono, Activo FROM Mesero WHERE Nombres LIKE ? OR DNI LIKE ? ORDER BY Nombres";
        List<Mesero> meseros = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like(filtro));
            ps.setString(2, like(filtro));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) meseros.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar meseros: " + e.getMessage(), e);
        }
        return meseros;
    }

    @Override
    public void save(Mesero m) {
        String sql = "INSERT INTO Mesero (Nombres, DNI, Telefono, Activo) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(m.getNombres()));
            ps.setString(2, nvl(m.getDni()));
            ps.setString(3, nvl(m.getTelefono()));
            ps.setBoolean(4, m.isActivo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar mesero: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Mesero m) {
        String sql = "UPDATE Mesero SET Nombres=?, DNI=?, Telefono=?, Activo=? WHERE IdMesero=?";
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nvl(m.getNombres()));
            ps.setString(2, nvl(m.getDni()));
            ps.setString(3, nvl(m.getTelefono()));
            ps.setBoolean(4, m.isActivo());
            ps.setInt(5, m.getIdMesero());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar mesero: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int idMesero) {
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement("DELETE FROM Mesero WHERE IdMesero=?")) {
            ps.setInt(1, idMesero);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar mesero: " + e.getMessage(), e);
        }
    }

    @Override public void deleteAll(List<Integer> ids) { deleteAllByIds(ids); }

    private Mesero mapRow(ResultSet rs) throws SQLException {
        return new Mesero(rs.getInt("IdMesero"), rs.getString("Nombres"), rs.getString("DNI"),
                rs.getString("Telefono"), rs.getBoolean("Activo"));
    }

    private String like(String value) { return "%" + nvl(value) + "%"; }
    private String nvl(String value) { return value == null ? "" : value.trim(); }

    private void deleteAllByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;
        StringBuilder sql = new StringBuilder("DELETE FROM Mesero WHERE IdMesero IN (");
        for (int i = 0; i < ids.size(); i++) sql.append(i == 0 ? "?" : ",?");
        sql.append(")");
        try (Connection conn = dbConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar meseros seleccionados: " + e.getMessage(), e);
        }
    }
}
