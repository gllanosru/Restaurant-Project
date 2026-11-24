package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Usuario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;

public class MenuController {
    @FXML
    private Button btnProductos;

    @FXML
    private Button btnMesas;

    @FXML
    private Button btnMeseros;

    @FXML
    private Button btnPedidos;

    @FXML
    private Button btnFacturas;

    @FXML
    private Button btnSalir;

    private Usuario usuarioActual;

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        aplicarPermisosPorRol();
    }

    private void aplicarPermisosPorRol() {
        ocultarTodos();

        if (usuarioActual == null || usuarioActual.getRol() == null) {
            return;
        }

        String rolNormalizado = usuarioActual.getRol().trim().toLowerCase(Locale.ROOT);

        if (rolNormalizado.contains("admin") || rolNormalizado.contains("gerente")) {
            mostrar(btnProductos, btnMesas, btnMeseros, btnFacturas, btnSalir);
        } else if (rolNormalizado.contains("cajero")) {
            mostrar(btnFacturas, btnSalir);
        } else if (rolNormalizado.contains("mozo")) {
            mostrar(btnMesas, btnPedidos, btnSalir);
        } else if (rolNormalizado.contains("cocinero")) {
            mostrar(btnProductos, btnPedidos, btnSalir);
        }
    }

    private void ocultarTodos() {
        configurarBoton(btnProductos, false);
        configurarBoton(btnMesas, false);
        configurarBoton(btnMeseros, false);
        configurarBoton(btnPedidos, false);
        configurarBoton(btnFacturas, false);
        configurarBoton(btnSalir, true);
    }

    private void mostrar(Button... botones) {
        for (Button boton : botones) {
            configurarBoton(boton, true);
        }
    }

    private void configurarBoton(Button boton, boolean visible) {
        if (boton != null) {
            boton.setVisible(visible);
            boton.setManaged(visible);
        }
    }

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