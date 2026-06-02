module com.ucv.restaurante {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.ucv.restaurante.controller to javafx.fxml;
    opens com.ucv.restaurante.model to javafx.base;

    exports com.ucv.restaurante.main;
}