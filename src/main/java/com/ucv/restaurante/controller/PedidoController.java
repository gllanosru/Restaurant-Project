package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Pedido;
import com.ucv.restaurante.service.IPedidoService;
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
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class PedidoController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<Pedido> tableView;
    @FXML private TableColumn<Pedido, Integer> colId;
    @FXML private TableColumn<Pedido, Integer> colMesa;
    @FXML private TableColumn<Pedido, String> colMesero;
    @FXML private TableColumn<Pedido, String> colEstado;
    @FXML private TableColumn<Pedido, Double> colTotal;
    @FXML private TableColumn<Pedido, LocalDateTime> colFecha;

    private final IPedidoService service;

    public PedidoController(IPedidoService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colMesa.setCellValueFactory(new PropertyValueFactory<>("numeroMesa"));
        colMesero.setCellValueFactory(new PropertyValueFactory<>("nombreMesero"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        Pedido pedido = tableView.getSelectionModel().getSelectedItem();
        if (pedido == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un pedido."); return; }
        abrirFormulario(pedido);
    }
    @FXML private void onEliminar() {
        Pedido pedido = tableView.getSelectionModel().getSelectedItem();
        if (pedido == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un pedido."); return; }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar el pedido seleccionado?")) return;
        service.eliminar(pedido.getIdPedido());
        cargarDatos(true);
    }

    private void cargarDatos(boolean alerta) {
        try {
            tableView.setItems(FXCollections.observableArrayList(service.buscar(txtFiltro == null ? "" : txtFiltro.getText())));
            lblTotal.setText("Total: " + tableView.getItems().size() + " pedido(s)");
        } catch (Exception e) {
            if (alerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/pedido-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            PedidoFormController controller = loader.getController();
            controller.setPedido(pedido);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(pedido == null ? "Nuevo pedido" : "Editar pedido");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }
}
