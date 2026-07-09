package com.ucv.restaurante;

import com.ucv.restaurante.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AppConfig context = AppConfig.getInstance();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/menu.fxml"));
        loader.setControllerFactory(context::getController);

        Scene scene = new Scene(loader.load(), 340, 520);
        stage.setTitle("Restaurante - Sistema de gestion");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        AppConfig.getInstance().destroy();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
