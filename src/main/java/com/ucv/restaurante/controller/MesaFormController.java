package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.Mesa;
import com.ucv.restaurante.service.IMesaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MesaFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtNumero;
    @FXML private TextField txtCapacidad;
    @FXML private ComboBox<String> cboEstado;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IMesaService service;
    private Mesa mesa;
    private Runnable onGuardar;

    public MesaFormController(IMesaService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        cboEstado.getItems().setAll("Libre", "Ocupada", "Reservada", "Mantenimiento");
        cboEstado.setValue("Libre");
        lblError.setVisible(false);
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
        lblTitulo.setText(mesa == null ? "Nueva mesa" : "Editar mesa");
        if (mesa != null) {
            txtNumero.setText(String.valueOf(mesa.getNumero()));
            txtCapacidad.setText(String.valueOf(mesa.getCapacidad()));
            cboEstado.setValue(mesa.getEstado());
        }
    }

    public void setOnGuardar(Runnable onGuardar) { this.onGuardar = onGuardar; }

    @FXML private void onGuardar() {
        try {
            Mesa m = mesa == null ? new Mesa() : mesa;
            m.setNumero(Integer.parseInt(txtNumero.getText().trim()));
            m.setCapacidad(Integer.parseInt(txtCapacidad.getText().trim()));
            m.setEstado(cboEstado.getValue());
            if (mesa == null) service.crear(m); else service.actualizar(m);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            lblError.setText(e instanceof NumberFormatException ? "Ingrese numeros validos." : e.getMessage());
            lblError.setVisible(true);
        }
    }

    @FXML private void onCancelar() { cerrar(); }
    private void cerrar() { ((Stage) btnCancelar.getScene().getWindow()).close(); }
}
