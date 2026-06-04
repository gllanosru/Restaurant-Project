package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class RestaurantController {

    @FXML private TextField txtMesa;
    @FXML private ComboBox<Mesero> cmbMesero;
    @FXML private Label lblTotalHistorial;

    // Una sola tabla para el menú (Platos y Bebidas juntos gracias a la herencia)
    @FXML private TableView<Producto> tvMenu;
    @FXML private TableColumn<Producto, String> colMenuNombre;
    @FXML private TableColumn<Producto, Double> colMenuPrecio;
    @FXML private TableColumn<Producto, Integer> colMenuCant;
    @FXML private TableColumn<Producto, Void> colMenuAcciones;

    // Tabla de historial
    @FXML private TableView<Pedido> tvHistorialGeneral;
    @FXML private TableColumn<Pedido, Integer> colHistorialMesa, colHistorialCantidad;
    @FXML private TableColumn<Pedido, String> colHistorialProducto;
    @FXML private TableColumn<Pedido, Double> colHistorialSubtotal;

    private GirasolRestaurante modelo;
    private final ObservableList<Pedido> registrosHistorial = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Obtener la base de datos (Singleton)
        modelo = GirasolRestaurante.getInstancia();

        // 2. Configurar columnas del Menú
        colMenuNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMenuPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colMenuCant.setCellValueFactory(new PropertyValueFactory<>("cantTemp"));
        inyectarBotonesAccion();

        // 3. Configurar columnas del Historial
        colHistorialMesa.setCellValueFactory(new PropertyValueFactory<>("numeroMesa"));
        colHistorialProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colHistorialCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colHistorialSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // 4. Cargar datos en la UI
        tvMenu.setItems(FXCollections.observableArrayList(modelo.getMenu()));
        tvHistorialGeneral.setItems(registrosHistorial);
        cmbMesero.getItems().addAll(modelo.getListaMeseros());
        cmbMesero.getSelectionModel().selectFirst();
    }

    private void inyectarBotonesAccion() {
        colMenuAcciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnMas = new Button("+");
            private final Button btnMenos = new Button("-");
            private final HBox layout = new HBox(btnMenos, btnMas);
            {
                layout.setSpacing(8);
                layout.setAlignment(Pos.CENTER);
                btnMas.setOnAction(e -> { getTableView().getItems().get(getIndex()).cambiarCant(1); tvMenu.refresh(); });
                btnMenos.setOnAction(e -> { getTableView().getItems().get(getIndex()).cambiarCant(-1); tvMenu.refresh(); });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : layout);
            }
        });
    }

    @FXML
    void onEnviarOrdenALaCocina() {
        String mesaStr = txtMesa.getText().trim();
        if (mesaStr.isEmpty()) { alerta("Error", "Ingrese número de mesa."); return; }

        try {
            int numMesa = Integer.parseInt(mesaStr);
            boolean ordenVacia = true;

            // Procesar los productos seleccionados
            for (Producto prod : tvMenu.getItems()) {
                if (prod.getCantTemp() > 0) {
                    Pedido nuevoPedido = new Pedido(numMesa, prod, prod.getCantTemp());

                    modelo.getListaPedidos().add(nuevoPedido);
                    registrosHistorial.add(nuevoPedido);

                    prod.cambiarCant(-prod.getCantTemp()); // Resetear cantidad elegida
                    ordenVacia = false;
                }
            }

            if (ordenVacia) {
                alerta("Orden vacía", "No has seleccionado ningún producto.");
                return;
            }

            tvMenu.refresh();
            actualizarTotalCaja();
            txtMesa.clear();
            alerta("Éxito", "Pedido enviado a cocina por: " + cmbMesero.getValue().getNombre());

        } catch (NumberFormatException e) {
            alerta("Error", "La mesa debe ser un número entero.");
        }
    }

    private void actualizarTotalCaja() {
        double total = registrosHistorial.stream().mapToDouble(Pedido::getSubtotal).sum();
        lblTotalHistorial.setText(String.format("S/ %.2f", total));
    }

    @FXML
    void onResetearTodo() {
        registrosHistorial.clear();
        modelo.getListaPedidos().clear();
        tvMenu.getItems().forEach(p -> p.cambiarCant(-p.getCantTemp()));
        tvMenu.refresh();
        actualizarTotalCaja();
        txtMesa.clear();
    }

    @FXML void onSalirAplicacion() { javafx.application.Platform.exit(); }

    private void alerta(String tit, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}