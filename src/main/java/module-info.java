module com.ucv.restaurante {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires com.microsoft.sqlserver.jdbc;

    opens com.ucv.restaurante to javafx.fxml;
    opens com.ucv.restaurante.controller to javafx.fxml;
    opens com.ucv.restaurante.model to javafx.base;

    exports com.ucv.restaurante;
    exports com.ucv.restaurante.controller;
    exports com.ucv.restaurante.model;
    exports com.ucv.restaurante.service;
    exports com.ucv.restaurante.repository;
    exports com.ucv.restaurante.config;
}
