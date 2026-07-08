package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Factura;
import com.ucv.restaurante.service.IFacturaService;
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

public class FacturaController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<Factura> tableView;
    @FXML private TableColumn<Factura, Integer> colId;
    @FXML private TableColumn<Factura, Integer> colPedido;
    @FXML private TableColumn<Factura, String> colCliente;
    @FXML private TableColumn<Factura, String> colRuc;
    @FXML private TableColumn<Factura, Double> colTotal;
    @FXML private TableColumn<Factura, String> colEstado;
    @FXML private TableColumn<Factura, LocalDateTime> colFecha;

    private final IFacturaService service;

    public FacturaController(IFacturaService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFactura"));
        colPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        Factura factura = tableView.getSelectionModel().getSelectedItem();
        if (factura == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione una factura."); return; }
        abrirFormulario(factura);
    }
    @FXML private void onEliminar() {
        Factura factura = tableView.getSelectionModel().getSelectedItem();
        if (factura == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione una factura."); return; }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar la factura seleccionada?")) return;
        service.eliminar(factura.getIdFactura());
        cargarDatos(true);
    }

    private void cargarDatos(boolean alerta) {
        try {
            tableView.setItems(FXCollections.observableArrayList(service.buscar(txtFiltro == null ? "" : txtFiltro.getText())));
            lblTotal.setText("Total: " + tableView.getItems().size() + " factura(s)");
        } catch (Exception e) {
            if (alerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(Factura factura) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/factura-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            FacturaFormController controller = loader.getController();
            controller.setFactura(factura);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(factura == null ? "Nueva factura" : "Editar factura");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }
}
