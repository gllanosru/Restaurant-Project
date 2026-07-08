package com.ucv.restaurante.controller;

import com.ucv.restaurante.model.Pedido;
import com.ucv.restaurante.model.PedidoDetalle;
import com.ucv.restaurante.model.Producto;
import com.ucv.restaurante.service.IPedidoDetalleService;
import com.ucv.restaurante.service.IPedidoService;
import com.ucv.restaurante.service.IProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PedidoFormController implements Initializable {
    @FXML private Label lblTitulo;
    @FXML private TextField txtMesa;
    @FXML private TextField txtMesero;
    @FXML private ComboBox<String> cboEstado;
    @FXML private ComboBox<Producto> cboProducto;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private Label lblTotalPedido;
    @FXML private TableView<PedidoDetalle> tblDetalle;
    @FXML private TableColumn<PedidoDetalle, String> colProducto;
    @FXML private TableColumn<PedidoDetalle, Integer> colCantidad;
    @FXML private TableColumn<PedidoDetalle, Double> colPrecio;
    @FXML private TableColumn<PedidoDetalle, Double> colSubtotal;
    @FXML private Label lblError;
    @FXML private Button btnCancelar;

    private final IPedidoService service;
    private final IPedidoDetalleService detalleService;
    private final IProductoService productoService;
    private final ObservableList<PedidoDetalle> detalles = FXCollections.observableArrayList();
    private Pedido pedido;
    private Runnable onGuardar;

    public PedidoFormController(IPedidoService service, IPedidoDetalleService detalleService, IProductoService productoService) {
        this.service = service;
        this.detalleService = detalleService;
        this.productoService = productoService;
    }

    @Override public void initialize(URL url, ResourceBundle rb) {
        cboEstado.getItems().setAll("Abierto", "Preparando", "Atendido", "Cancelado");
        cboEstado.setValue("Abierto");
        cboProducto.setItems(FXCollections.observableArrayList(productoService.listar()));
        cboProducto.valueProperty().addListener((obs, oldValue, producto) -> {
            if (producto != null) txtPrecio.setText(String.valueOf(producto.getPrecio()));
        });
        colProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblDetalle.setItems(detalles);
        txtCantidad.setText("1");
        actualizarTotal();
        lblError.setVisible(false);
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        lblTitulo.setText(pedido == null ? "Nuevo pedido" : "Editar pedido");
        if (pedido != null) {
            txtMesa.setText(String.valueOf(pedido.getNumeroMesa()));
            txtMesero.setText(pedido.getNombreMesero());
            cboEstado.setValue(pedido.getEstado());
            detalles.setAll(detalleService.buscarPorPedido(pedido.getIdPedido()));
            actualizarTotal();
        }
    }

    public void setOnGuardar(Runnable onGuardar) { this.onGuardar = onGuardar; }

    @FXML private void onGuardar() {
        try {
            Pedido p = pedido == null ? new Pedido() : pedido;
            p.setNumeroMesa(Integer.parseInt(txtMesa.getText().trim()));
            p.setNombreMesero(txtMesero.getText());
            p.setTotal(calcularTotal());
            p.setEstado(cboEstado.getValue());
            int idPedido;
            if (pedido == null) {
                idPedido = service.crearRetornandoId(p);
                p.setIdPedido(idPedido);
            } else {
                service.actualizar(p);
                idPedido = p.getIdPedido();
            }
            detalleService.reemplazarPorPedido(idPedido, detalles);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            lblError.setText(e instanceof NumberFormatException ? "Ingrese numeros validos." : e.getMessage());
            lblError.setVisible(true);
        }
    }

    @FXML private void onAgregarDetalle() {
        try {
            Producto producto = cboProducto.getValue();
            if (producto == null) throw new IllegalArgumentException("Seleccione un producto.");
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setIdProducto(producto.getIdProducto());
            detalle.setProducto(producto.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precio);
            detalle.setSubtotal(cantidad * precio);
            detalles.add(detalle);
            actualizarTotal();
            lblError.setVisible(false);
        } catch (Exception e) {
            lblError.setText(e instanceof NumberFormatException ? "Ingrese cantidad y precio validos." : e.getMessage());
            lblError.setVisible(true);
        }
    }

    @FXML private void onQuitarDetalle() {
        PedidoDetalle detalle = tblDetalle.getSelectionModel().getSelectedItem();
        if (detalle == null) {
            lblError.setText("Seleccione un producto del detalle.");
            lblError.setVisible(true);
            return;
        }
        detalles.remove(detalle);
        actualizarTotal();
    }

    @FXML private void onCancelar() { cerrar(); }
    private void cerrar() { ((Stage) btnCancelar.getScene().getWindow()).close(); }

    private double calcularTotal() {
        double total = 0;
        for (PedidoDetalle detalle : detalles) total += detalle.getCantidad() * detalle.getPrecioUnitario();
        return total;
    }

    private void actualizarTotal() {
        lblTotalPedido.setText(String.format("Total: S/ %.2f", calcularTotal()));
    }
}
