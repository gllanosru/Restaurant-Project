package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.Producto;
import com.ucv.restaurante.service.IProductoService;
import com.ucv.restaurante.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductoFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private CheckBox chkActivo;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IProductoService service;
    private Producto producto;
    private Runnable onGuardar;

    public ProductoFormController(IProductoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chkActivo.setSelected(true);
        lblError.setVisible(false);
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto == null) {
            lblTitulo.setText("Nuevo producto");
            return;
        }
        lblTitulo.setText("Editar producto");
        txtNombre.setText(producto.getNombre());
        txtCategoria.setText(producto.getCategoria());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getStock()));
        chkActivo.setSelected(producto.isActivo());
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        try {
            Producto p = producto == null ? new Producto() : producto;
            p.setNombre(txtNombre.getText());
            p.setCategoria(txtCategoria.getText());
            p.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
            p.setStock(Integer.parseInt(txtStock.getText().trim()));
            p.setActivo(chkActivo.isSelected());
            if (producto == null) service.crear(p); else service.actualizar(p);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (NumberFormatException e) {
            mostrarError("Ingrese precio y stock validos.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (Exception e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }

    @FXML private void onCancelar() { cerrar(); }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void cerrar() {
        ((Stage) btnCancelar.getScene().getWindow()).close();
    }
}
