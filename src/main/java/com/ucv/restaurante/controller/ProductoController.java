package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Producto;
import com.ucv.restaurante.service.IProductoService;
import com.ucv.restaurante.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductoController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<Producto> tableView;
    @FXML private TableColumn<Producto, Integer> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, Boolean> colActivo;

    private final IProductoService service;
    private final ObservableList<Producto> data = FXCollections.observableArrayList();

    public ProductoController(IProductoService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        tableView.setItems(data);
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        Producto producto = tableView.getSelectionModel().getSelectedItem();
        if (producto == null) {
            AlertUtil.advertencia("Sin seleccion", "Seleccione un producto.");
            return;
        }
        abrirFormulario(producto);
    }

    @FXML private void onEliminar() {
        Producto producto = tableView.getSelectionModel().getSelectedItem();
        if (producto == null) {
            AlertUtil.advertencia("Sin seleccion", "Seleccione un producto.");
            return;
        }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar el producto seleccionado?")) return;
        try {
            service.eliminar(producto.getIdProducto());
            cargarDatos(true);
        } catch (IllegalStateException e) {
            AlertUtil.advertencia("Producto relacionado", e.getMessage());
        } catch (Exception e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }

    private void cargarDatos(boolean mostrarAlerta) {
        try {
            data.setAll(service.buscar(txtFiltro == null ? "" : txtFiltro.getText()));
            lblTotal.setText("Total: " + data.size() + " producto(s)");
        } catch (Exception e) {
            data.clear();
            lblTotal.setText("No se pudo cargar productos.");
            if (mostrarAlerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/producto-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            ProductoFormController controller = loader.getController();
            controller.setProducto(producto);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(producto == null ? "Nuevo producto" : "Editar producto");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "No se pudo abrir el formulario:\n" + e.getMessage());
        }
    }
}
