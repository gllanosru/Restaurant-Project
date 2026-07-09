package com.ucv.restaurante.config;

import com.ucv.restaurante.controller.*;
import com.ucv.restaurante.repository.*;
import com.ucv.restaurante.service.*;

public class AppConfig {
    private static AppConfig instance;

    private final DatabaseConfig databaseConfig;
    private final IProductoService productoService;
    private final IMesaService mesaService;
    private final IMeseroService meseroService;
    private final IPedidoService pedidoService;
    private final IPedidoDetalleService pedidoDetalleService;
    private final IFacturaService facturaService;

    private AppConfig() {
        databaseConfig = new DatabaseConfig();
        IProductoRepository productoRepository = new ProductoRepository(databaseConfig);
        IMesaRepository mesaRepository = new MesaRepository(databaseConfig);
        IMeseroRepository meseroRepository = new MeseroRepository(databaseConfig);
        IPedidoRepository pedidoRepository = new PedidoRepository(databaseConfig);
        IPedidoDetalleRepository pedidoDetalleRepository = new PedidoDetalleRepository(databaseConfig);
        IFacturaRepository facturaRepository = new FacturaRepository(databaseConfig);

        productoService = new ProductoService(productoRepository);
        mesaService = new MesaService(mesaRepository);
        meseroService = new MeseroService(meseroRepository);
        pedidoService = new PedidoService(pedidoRepository);
        pedidoDetalleService = new PedidoDetalleService(pedidoDetalleRepository);
        facturaService = new FacturaService(facturaRepository);
    }

    public static AppConfig getInstance() {
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    public Object getController(Class<?> type) {
        if (type == ProductoController.class) return new ProductoController(productoService);
        if (type == ProductoFormController.class) return new ProductoFormController(productoService);
        if (type == MesaController.class) return new MesaController(mesaService);
        if (type == MesaFormController.class) return new MesaFormController(mesaService);
        if (type == MeseroController.class) return new MeseroController(meseroService);
        if (type == MeseroFormController.class) return new MeseroFormController(meseroService);
        if (type == PedidoController.class) return new PedidoController(pedidoService);
        if (type == PedidoFormController.class) return new PedidoFormController(pedidoService, pedidoDetalleService, productoService);
        if (type == FacturaController.class) return new FacturaController(facturaService);
        if (type == FacturaFormController.class) return new FacturaFormController(facturaService);
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador: " + type.getName(), e);
        }
    }

    public void destroy() {
        databaseConfig.close();
    }
}
