USE Restaurante;
GO

IF OBJECT_ID('PedidoDetalle', 'U') IS NULL
BEGIN
    CREATE TABLE PedidoDetalle (
        IdDetalle INT IDENTITY(1,1) PRIMARY KEY,
        IdPedido INT NOT NULL,
        IdProducto INT NOT NULL,
        Cantidad INT NOT NULL,
        PrecioUnitario DECIMAL(10,2) NOT NULL,
        Subtotal AS (Cantidad * PrecioUnitario) PERSISTED,

        CONSTRAINT FK_PedidoDetalle_Pedido
            FOREIGN KEY (IdPedido) REFERENCES Pedido(IdPedido),

        CONSTRAINT FK_PedidoDetalle_Producto
            FOREIGN KEY (IdProducto) REFERENCES Producto(IdProducto),

        CONSTRAINT CK_PedidoDetalle_Cantidad
            CHECK (Cantidad > 0),

        CONSTRAINT CK_PedidoDetalle_PrecioUnitario
            CHECK (PrecioUnitario >= 0)
    );
END
GO

IF IS_ROLEMEMBER('db_datareader', 'adm') = 0
BEGIN
    ALTER ROLE db_datareader ADD MEMBER adm;
END
GO

IF IS_ROLEMEMBER('db_datawriter', 'adm') = 0
BEGIN
    ALTER ROLE db_datawriter ADD MEMBER adm;
END
GO
