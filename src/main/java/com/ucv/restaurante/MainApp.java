package com.ucv.restaurante;

import com.ucv.restaurante.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ucv/restaurante/login.fxml"));
        stage.setScene(new Scene(loader.load()));
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
