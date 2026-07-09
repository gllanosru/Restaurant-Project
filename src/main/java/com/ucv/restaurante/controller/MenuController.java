package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private void abrirProductos() {
        abrirVentana("/com/ucv/restaurante/producto-view.fxml", "Restaurante - Productos");
    }

    @FXML
    private void abrirMesas() {
        abrirVentana("/com/ucv/restaurante/mesa-view.fxml", "Restaurante - Mesas");
    }

    @FXML
    private void abrirMeseros() {
        abrirVentana("/com/ucv/restaurante/mesero-view.fxml", "Restaurante - Meseros");
    }

    @FXML
    private void abrirPedidos() {
        abrirVentana("/com/ucv/restaurante/pedido-view.fxml", "Restaurante - Pedidos");
    }

    @FXML
    private void abrirFacturas() {
        abrirVentana("/com/ucv/restaurante/factura-view.fxml", "Restaurante - Facturas");
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salir() {
        Platform.exit();
    }
}
