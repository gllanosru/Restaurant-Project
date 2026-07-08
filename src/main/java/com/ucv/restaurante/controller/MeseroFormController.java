package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.Mesero;
import com.ucv.restaurante.service.IMeseroService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MeseroFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IMeseroService service;
    private Mesero mesero;
    private Runnable onGuardar;

    public MeseroFormController(IMeseroService service) { this.service = service; }
    @Override public void initialize(URL url, ResourceBundle rb) { chkActivo.setSelected(true); lblError.setVisible(false); }

    public void setMesero(Mesero mesero) {
        this.mesero = mesero;
        lblTitulo.setText(mesero == null ? "Nuevo mesero" : "Editar mesero");
        if (mesero != null) {
            txtNombres.setText(mesero.getNombres());
            txtDni.setText(mesero.getDni());
            txtTelefono.setText(mesero.getTelefono());
            chkActivo.setSelected(mesero.isActivo());
        }
    }

    public void setOnGuardar(Runnable onGuardar) { this.onGuardar = onGuardar; }

    @FXML private void onGuardar() {
        try {
            Mesero m = mesero == null ? new Mesero() : mesero;
            m.setNombres(txtNombres.getText());
            m.setDni(txtDni.getText());
            m.setTelefono(txtTelefono.getText());
            m.setActivo(chkActivo.isSelected());
            if (mesero == null) service.crear(m); else service.actualizar(m);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
        }
    }

    @FXML private void onCancelar() { cerrar(); }
    private void cerrar() { ((Stage) btnCancelar.getScene().getWindow()).close(); }
}
