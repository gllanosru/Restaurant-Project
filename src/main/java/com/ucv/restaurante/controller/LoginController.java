package com.ucv.restaurante.controller;

import com.ucv.restaurante.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    private final UsuarioService service = new UsuarioService();

    @FXML
    private void onIngresar() {
        if (service.autenticar(txtUsuario.getText(), txtPassword.getText())) {
            // Lógica para cerrar login y abrir menú
            ((Stage) txtUsuario.getScene().getWindow()).close();
        } else {
            // Mostrar error
        }
    }
}