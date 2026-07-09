package com.ucv.restaurante.controller;

import com.ucv.restaurante.service.UsuarioService;
import com.ucv.restaurante.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    private final UsuarioService service = new UsuarioService();

    @FXML
    private void onIngresar() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        if (usuario.isBlank() || password.isBlank()) {
            AlertUtil.advertencia("Login", "Ingrese usuario y contrasena.");
            return;
        }

        try {
            if (service.autenticar(usuario, password)) {
                abrirMenu();
            } else {
                AlertUtil.advertencia("Login", "Usuario o contrasena incorrectos.");
            }
        } catch (RuntimeException e) {
            AlertUtil.error("Error de base de datos", e.getMessage());
        }
    }

    private void abrirMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/menu.fxml"));
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("Restaurante - Menu");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el menu: " + e.getMessage());
        }
    }
}
