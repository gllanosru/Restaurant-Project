package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import com.ucv.restaurante.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public boolean validar(String user, String pass) {
        String sql = "SELECT COUNT(*) FROM Usuario WHERE Username = ? AND Password = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Usuario buscarPorCredenciales(String user, String pass) {
        String sql = "SELECT IdUsuario, Username, Password, NombreCompleto, Rol FROM Usuario WHERE Username = ? AND Password = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("IdUsuario"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getString("NombreCompleto"),
                            rs.getString("Rol")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar usuario: " + e.getMessage(), e);
        }
        return null;
    }
}