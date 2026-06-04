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

    //Componentes de la Vista (Cosas que están en la pantalla)
    @FXML private TextField txtMesa;
    @FXML private ComboBox<Mesero> cmbMesero;
    @FXML private Label lblTotalHistorial;

    // Tabla de Platos
    @FXML private TableView<Producto> tvSeleccionPlatos;
    @FXML private TableColumn<Producto, String> colPlatoNombre;
    @FXML private TableColumn<Producto, Double> colPlatoPrecio;
    @FXML private TableColumn<Producto, Integer> colPlatoCant;
    @FXML private TableColumn<Producto, Void> colPlatoAcciones;

    // Tabla de Bebidas
    @FXML private TableView<Producto> tvSeleccionBebidas;
    @FXML private TableColumn<Producto, String> colBebidaNombre;
    @FXML private TableColumn<Producto, Double> colBebidaPrecio;
    @FXML private TableColumn<Producto, Integer> colBebidaCant;
    @FXML private TableColumn<Producto, Void> colBebidaAcciones;

    // Tabla del Historial (Segunda pestaña)
    @FXML private TableView<Pedido> tvHistorialGeneral;
    @FXML private TableColumn<Pedido, Integer> colHistorialMesa, colHistorialCantidad;
    @FXML private TableColumn<Pedido, String> colHistorialProducto;
    @FXML private TableColumn<Pedido, Double> colHistorialSubtotal;

    // Listas para guardar la información temporalmente
    private GirasolRestaurante modelo;
    private ObservableList<Pedido> registrosHistorial = FXCollections.observableArrayList();
    private ObservableList<Producto> datosPlatos = FXCollections.observableArrayList();
    private ObservableList<Producto> datosBebidas = FXCollections.observableArrayList();

    //Metodo que se ejecuta solito al abrir la ventana
    @FXML
    public void initialize() {
        // Conectar la logica/datos usando el patrón Singleton
        modelo = GirasolRestaurante.getInstancia();

        // Configura columnas de la Tabla Platos
        colPlatoNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPlatoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPlatoCant.setCellValueFactory(new PropertyValueFactory<>("cantTemp"));
        ponerBotonesFalsos(colPlatoAcciones);

        // Configura columnas de la Tabla Bebidas
        colBebidaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colBebidaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colBebidaCant.setCellValueFactory(new PropertyValueFactory<>("cantTemp"));
        ponerBotonesFalsos(colBebidaAcciones);

        // Configura columnas del Historial
        colHistorialMesa.setCellValueFactory(new PropertyValueFactory<>("numeroMesa"));
        colHistorialProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colHistorialCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colHistorialSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        // Separar el menú: lo que es Plato va a una lista, lo que es Bebida va a la otra
        for (Producto p : modelo.getMenu()) {
            if (p instanceof Plato) {
                datosPlatos.add(p);
            } else {
                datosBebidas.add(p);
            }
        }

        // Mostrar los datos guardados en las tablas y el combo de la pantalla.
        tvSeleccionPlatos.setItems(datosPlatos);
        tvSeleccionBebidas.setItems(datosBebidas);
        tvHistorialGeneral.setItems(registrosHistorial);
        // cmbMesero.getItems().addAll(modelo.getListaMeseros()); //Aqui apareceran la lista de los empleados.
    }

    // Metodo para dibujar los botones "+" y "-".
    private void ponerBotonesFalsos(TableColumn<Producto, Void> columna) {
        columna.setCellFactory(param -> new TableCell<>() {
            private final Button btnMas = new Button("+");
            private final Button btnMenos = new Button("-");
            private final HBox contenedor = new HBox(btnMenos, btnMas);
            {
                contenedor.setSpacing(8);
                contenedor.setAlignment(Pos.CENTER);
                // Aquí NO hay código. Al hacer clic, no pasará nada.
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : contenedor);
            }
        });
    }

    // Metodo para Enviar Orden

    @FXML
    void onEnviarOrdenALaCocina() {
        int numMesa = Integer.parseInt(txtMesa.getText());  // Leer el número de mesa de la pantalla y pasarlo a número entero
        double totalAcumulado = 0;

        // Simularemos que agregamos 1 Ceviche al historial por defecto
        for (Producto prod : datosPlatos) {
            if (prod.getNombre().equals("Ceviche Clásico")) {
                Pedido p = new Pedido(numMesa, prod, 1);
                registrosHistorial.add(p);
            }
        }


        // Calcular el total de dinero sumando de forma tradicional utilizando (Bucle For)
        for (Pedido ped : registrosHistorial) {
            totalAcumulado = totalAcumulado + ped.getSubtotal();
        }

        // Mostrar el total en la etiqueta de la pantalla
        lblTotalHistorial.setText("S/ " + totalAcumulado);
        txtMesa.clear();
    }

    //Metodo para refrescar el programa
    @FXML
    void onResetearTodo() {
        registrosHistorial.clear();
        lblTotalHistorial.setText("S/ 0.00");
        txtMesa.clear();
    }

    // Metodo para cerrar el sistema
    @FXML
    void onSalirAplicacion() {
        javafx.application.Platform.exit();
    }
}