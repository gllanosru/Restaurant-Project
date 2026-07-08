package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Mesa;
import com.ucv.restaurante.service.IMesaService;
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

public class MesaController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<Mesa> tableView;
    @FXML private TableColumn<Mesa, Integer> colId;
    @FXML private TableColumn<Mesa, Integer> colNumero;
    @FXML private TableColumn<Mesa, Integer> colCapacidad;
    @FXML private TableColumn<Mesa, String> colEstado;

    private final IMesaService service;

    public MesaController(IMesaService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMesa"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        Mesa mesa = tableView.getSelectionModel().getSelectedItem();
        if (mesa == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione una mesa."); return; }
        abrirFormulario(mesa);
    }
    @FXML private void onEliminar() {
        Mesa mesa = tableView.getSelectionModel().getSelectedItem();
        if (mesa == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione una mesa."); return; }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar la mesa seleccionada?")) return;
        service.eliminar(mesa.getIdMesa());
        cargarDatos(true);
    }

    private void cargarDatos(boolean alerta) {
        try {
            tableView.setItems(FXCollections.observableArrayList(service.buscar(txtFiltro == null ? "" : txtFiltro.getText())));
            lblTotal.setText("Total: " + tableView.getItems().size() + " mesa(s)");
        } catch (Exception e) {
            if (alerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(Mesa mesa) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/mesa-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            MesaFormController controller = loader.getController();
            controller.setMesa(mesa);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(mesa == null ? "Nueva mesa" : "Editar mesa");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }
}
