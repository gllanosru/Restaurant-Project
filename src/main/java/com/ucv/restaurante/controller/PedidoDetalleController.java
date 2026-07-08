package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.PedidoDetalle;
import com.ucv.restaurante.service.IPedidoDetalleService;
import com.ucv.restaurante.util.AlertUtil;
import javafx.collections.FXCollections;
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

public class PedidoDetalleController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<PedidoDetalle> tableView;
    @FXML private TableColumn<PedidoDetalle, Integer> colId;
    @FXML private TableColumn<PedidoDetalle, Integer> colPedido;
    @FXML private TableColumn<PedidoDetalle, Integer> colProductoId;
    @FXML private TableColumn<PedidoDetalle, String> colProducto;
    @FXML private TableColumn<PedidoDetalle, Integer> colCantidad;
    @FXML private TableColumn<PedidoDetalle, Double> colPrecio;
    @FXML private TableColumn<PedidoDetalle, Double> colSubtotal;

    private final IPedidoDetalleService service;

    public PedidoDetalleController(IPedidoDetalleService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDetalle"));
        colPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colProductoId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        PedidoDetalle detalle = tableView.getSelectionModel().getSelectedItem();
        if (detalle == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un detalle."); return; }
        abrirFormulario(detalle);
    }
    @FXML private void onEliminar() {
        PedidoDetalle detalle = tableView.getSelectionModel().getSelectedItem();
        if (detalle == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un detalle."); return; }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar el detalle seleccionado?")) return;
        service.eliminar(detalle.getIdDetalle());
        cargarDatos(true);
    }

    private void cargarDatos(boolean alerta) {
        try {
            tableView.setItems(FXCollections.observableArrayList(service.buscar(txtFiltro == null ? "" : txtFiltro.getText())));
            lblTotal.setText("Total: " + tableView.getItems().size() + " detalle(s)");
        } catch (Exception e) {
            lblTotal.setText("No se pudo cargar detalles.");
            if (alerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(PedidoDetalle detalle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/pedido-detalle-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            PedidoDetalleFormController controller = loader.getController();
            controller.setDetalle(detalle);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(detalle == null ? "Nuevo detalle" : "Editar detalle");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }
}
