package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.PedidoDetalle;
import com.ucv.restaurante.service.IPedidoDetalleService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PedidoDetalleFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtPedido;
    @FXML private TextField txtProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IPedidoDetalleService service;
    private PedidoDetalle detalle;
    private Runnable onGuardar;

    public PedidoDetalleFormController(IPedidoDetalleService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblError.setVisible(false);
    }

    public void setDetalle(PedidoDetalle detalle) {
        this.detalle = detalle;
        lblTitulo.setText(detalle == null ? "Nuevo detalle de pedido" : "Editar detalle de pedido");
        if (detalle != null) {
            txtPedido.setText(String.valueOf(detalle.getIdPedido()));
            txtProducto.setText(String.valueOf(detalle.getIdProducto()));
            txtCantidad.setText(String.valueOf(detalle.getCantidad()));
            txtPrecio.setText(String.valueOf(detalle.getPrecioUnitario()));
        }
    }

    public void setOnGuardar(Runnable onGuardar) { this.onGuardar = onGuardar; }

    @FXML
    private void onGuardar() {
        try {
            PedidoDetalle d = detalle == null ? new PedidoDetalle() : detalle;
            d.setIdPedido(Integer.parseInt(txtPedido.getText().trim()));
            d.setIdProducto(Integer.parseInt(txtProducto.getText().trim()));
            d.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            d.setPrecioUnitario(Double.parseDouble(txtPrecio.getText().trim()));
            if (detalle == null) service.crear(d); else service.actualizar(d);
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
