package com.ucv.restaurante.config;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

public class DatabaseConfig implements AutoCloseable {
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;"
            + "databaseName=Restaurante;"
            + "user=adminUCV;"
            + "password=123456789;"
            + "trustServerCertificate=true;"
            + "encrypt=false;";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    @Override
    public void close() {
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                DriverManager.deregisterDriver(drivers.nextElement());
            }
        } catch (Exception ignored) {
        }
    }
}
