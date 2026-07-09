package com.ucv.restaurante.repository;

import com.ucv.restaurante.config.DatabaseConfig;
import java.sql.*;

public class UsuarioRepository {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    public boolean validar(String user, String pass) {
        String sql = "SELECT COUNT(*) FROM Usuario WHERE Username = ? AND Password = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error BD: " + e.getMessage());
        }
    }
}