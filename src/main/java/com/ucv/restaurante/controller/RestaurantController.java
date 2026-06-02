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
    @FXML private Label lblTotalHistorial;
    @FXML private TableView<Producto> tvSeleccionPlatos, tvSeleccionBebidas;
    @FXML private TableColumn<Producto, String> colPlatoNombre, colBebidaNombre;
    @FXML private TableColumn<Producto, Double> colPlatoPrecio, colBebidaPrecio;
    @FXML private TableColumn<Producto, Integer> colPlatoCant, colBebidaCant;
    @FXML private TableColumn<Producto, Void> colPlatoAcciones, colBebidaAcciones;

    @FXML private TableView<Pedido> tvHistorialGeneral;
    @FXML private TableColumn<Pedido, Integer> colHistorialMesa, colHistorialCantidad;
    @FXML private TableColumn<Pedido, String> colHistorialProducto;
    @FXML private TableColumn<Pedido, Double> colHistorialSubtotal;

    private GirasolRestaurante modelo;
    private final ObservableList<Producto> datosPlatos = FXCollections.observableArrayList();
    private final ObservableList<Producto> datosBebidas = FXCollections.observableArrayList();
    private ObservableList<Pedido> registrosHistorial;

    @FXML
    public void initialize() {
        modelo = GirasolRestaurante.getInstancia();
        registrosHistorial = FXCollections.observableArrayList(modelo.getListaPedidos());

        // Enlace automático a las celdas usando las propiedades simplificadas
        colPlatoNombre.setCellValueFactory(new PropertyValueFactory<>("nombreDetallado"));
        colPlatoPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnidad"));
        colPlatoCant.setCellValueFactory(new PropertyValueFactory<>("cantTemp"));

        colBebidaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreDetallado"));
        colBebidaPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnidad"));
        colBebidaCant.setCellValueFactory(new PropertyValueFactory<>("cantTemp"));

        colHistorialMesa.setCellValueFactory(new PropertyValueFactory<>("numeroMesa"));
        colHistorialProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colHistorialCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colHistorialSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tvHistorialGeneral.setItems(registrosHistorial);

        // Clasificación directa de productos
        for (Producto p : modelo.getMenu()) {
            if (p instanceof Plato) datosPlatos.add(p);
            else datosBebidas.add(p);
        }

        inyectarAcciones(colPlatoAcciones);
        inyectarAcciones(colBebidaAcciones);

        tvSeleccionPlatos.setItems(datosPlatos);
        tvSeleccionBebidas.setItems(datosBebidas);
        actualizarTotalCaja();
    }

    private void inyectarAcciones(TableColumn<Producto, Void> columna) {
        columna.setCellFactory(param -> new TableCell<>() {
            private final Button btnMas = new Button("+"), btnMenos = new Button("-");
            private final HBox layout = new HBox(btnMenos, btnMas);
            {
                layout.setSpacing(8); layout.setAlignment(Pos.CENTER);
                btnMas.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
                btnMenos.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
                btnMas.setOnAction(e -> { getTableView().getItems().get(getIndex()).cambiarCant(1); getTableView().refresh(); });
                btnMenos.setOnAction(e -> { getTableView().getItems().get(getIndex()).cambiarCant(-1); getTableView().refresh(); });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty); setGraphic(empty ? null : layout);
            }
        });
    }

    @FXML
    void onEnviarOrdenALaCocina() {
        String mesaStr = txtMesa.getText().trim();
        if (mesaStr.isEmpty()) { alerta("Mesa Vacía", "Por favor ponga una mesa."); return; }

        try {
            int numMesa = Integer.parseInt(mesaStr);
            if (numMesa <= 0) throw new NumberFormatException();

            // Unificamos el envío sumando el resultado booleano de ambas listas
            boolean enviado = mandarComanda(datosPlatos, numMesa) | mandarComanda(datosBebidas, numMesa);

            if (!enviado) { alerta("Orden vacía", "No has seleccionado ningún producto con (+)."); return; }

            tvSeleccionPlatos.refresh(); tvSeleccionBebidas.refresh();
            actualizarTotalCaja(); txtMesa.clear();

            new Alert(Alert.AlertType.INFORMATION, "Pedido enviado al historial de la Mesa " + numMesa, ButtonType.OK).showAndWait();
        } catch (NumberFormatException e) {
            alerta("Error", "La mesa debe ser un número entero válido.");
        }
    }

    private boolean mandarComanda(ObservableList<Producto> lista, int mesa) {
        boolean flag = false;
        for (Producto prod : lista) {
            if (prod.getCantTemp() > 0) {
                Pedido p = new Pedido(mesa, prod, prod.getCantTemp());
                modelo.getListaPedidos().add(p);
                registrosHistorial.add(p);
                prod.cambiarCant(-prod.getCantTemp()); // Regresa la fila a 0 unidades
                flag = true;
            }
        }
        return flag;
    }

    private void actualizarTotalCaja() {
        double total = registrosHistorial.stream().mapToDouble(Pedido::getSubtotal).sum();
        lblTotalHistorial.setText(String.format("S/ %.2f", total));
    }

    @FXML
    void onResetearTodo() {
        modelo.getListaPedidos().clear(); registrosHistorial.clear();
        datosPlatos.forEach(p -> p.cambiarCant(-p.getCantTemp()));
        datosBebidas.forEach(b -> b.cambiarCant(-b.getCantTemp()));
        tvSeleccionPlatos.refresh(); tvSeleccionBebidas.refresh();
        actualizarTotalCaja(); txtMesa.clear();
    }

    @FXML void onSalirAplicacion() { javafx.application.Platform.exit(); }

    private void alerta(String tit, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setTitle(tit); a.setHeaderText(null); a.showAndWait();
    }
}