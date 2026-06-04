package com.ucv.restaurante.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        java.net.URL fxmlLocation = getClass().getResource("/com/ucv/restaurante/view/RestaurantView.fxml");

        if (fxmlLocation == null) {
            System.err.println("¡ERROR CRÍTICO! El archivo FXML no se encontró.");
            System.err.println("Revisa que esté dentro de resources/com/ucv/restaurante/view/");
            return;
        }



        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(loader.load());


        primaryStage.setTitle("Restaurante - Panel de historial de Pedidos");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}