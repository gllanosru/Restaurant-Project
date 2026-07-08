package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.Factura;
import com.ucv.restaurante.service.IFacturaService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FacturaFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtPedido;
    @FXML private TextField txtCliente;
    @FXML private TextField txtRuc;
    @FXML private TextField txtTotal;
    @FXML private ComboBox<String> cboEstado;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IFacturaService service;
    private Factura factura;
    private Runnable onGuardar;

    public FacturaFormController(IFacturaService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        cboEstado.getItems().setAll("Emitida", "Pagada", "Anulada");
        cboEstado.setValue("Emitida");
        lblError.setVisible(false);
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
        lblTitulo.setText(factura == null ? "Nueva factura" : "Editar factura");
        if (factura != null) {
            txtPedido.setText(String.valueOf(factura.getIdPedido()));
            txtCliente.setText(factura.getCliente());
            txtRuc.setText(factura.getRuc());
            txtTotal.setText(String.valueOf(factura.getTotal()));
            cboEstado.setValue(factura.getEstado());
        }
    }

    public void setOnGuardar(Runnable onGuardar) { this.onGuardar = onGuardar; }

    @FXML private void onGuardar() {
        try {
            Factura f = factura == null ? new Factura() : factura;
            f.setIdPedido(Integer.parseInt(txtPedido.getText().trim()));
            f.setCliente(txtCliente.getText());
            f.setRuc(txtRuc.getText());
            f.setTotal(Double.parseDouble(txtTotal.getText().trim()));
            f.setEstado(cboEstado.getValue());
            if (factura == null) service.crear(f); else service.actualizar(f);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            lblError.setText(e instanceof NumberFormatException ? "Ingrese pedido y total validos." : e.getMessage());
            lblError.setVisible(true);
        }
    }

    @FXML private void onCancelar() { cerrar(); }
    private void cerrar() { ((Stage) btnCancelar.getScene().getWindow()).close(); }
}
