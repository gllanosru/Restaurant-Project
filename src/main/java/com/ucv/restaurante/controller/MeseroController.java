package com.ucv.restaurante.controller;

import com.ucv.restaurante.config.AppConfig;
import com.ucv.restaurante.model.Mesero;
import com.ucv.restaurante.service.IMeseroService;
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

public class MeseroController implements Initializable {
    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;
    @FXML private TableView<Mesero> tableView;
    @FXML private TableColumn<Mesero, Integer> colId;
    @FXML private TableColumn<Mesero, String> colNombres;
    @FXML private TableColumn<Mesero, String> colDni;
    @FXML private TableColumn<Mesero, String> colTelefono;
    @FXML private TableColumn<Mesero, Boolean> colActivo;

    private final IMeseroService service;

    public MeseroController(IMeseroService service) { this.service = service; }

    @Override public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMesero"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));
        cargarDatos(false);
    }

    @FXML private void onBuscar() { cargarDatos(true); }
    @FXML private void onCrear() { abrirFormulario(null); }
    @FXML private void onEditar() {
        Mesero mesero = tableView.getSelectionModel().getSelectedItem();
        if (mesero == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un mesero."); return; }
        abrirFormulario(mesero);
    }
    @FXML private void onEliminar() {
        Mesero mesero = tableView.getSelectionModel().getSelectedItem();
        if (mesero == null) { AlertUtil.advertencia("Sin seleccion", "Seleccione un mesero."); return; }
        if (!AlertUtil.confirmar("Confirmar eliminacion", "Desea eliminar el mesero seleccionado?")) return;
        service.eliminar(mesero.getIdMesero());
        cargarDatos(true);
    }

    private void cargarDatos(boolean alerta) {
        try {
            tableView.setItems(FXCollections.observableArrayList(service.buscar(txtFiltro == null ? "" : txtFiltro.getText())));
            lblTotal.setText("Total: " + tableView.getItems().size() + " mesero(s)");
        } catch (Exception e) {
            if (alerta) AlertUtil.error("Error", e.getMessage());
        }
    }

    private void abrirFormulario(Mesero mesero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/mesero-form.fxml"));
            loader.setControllerFactory(AppConfig.getInstance()::getController);
            Parent root = loader.load();
            MeseroFormController controller = loader.getController();
            controller.setMesero(mesero);
            controller.setOnGuardar(() -> cargarDatos(true));
            Stage modal = new Stage();
            modal.setTitle(mesero == null ? "Nuevo mesero" : "Editar mesero");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setResizable(false);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", e.getMessage());
        }
    }
}
