package main.java;

import main.java.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.model.Player;
import main.java.network.ConnectionHandler;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Battleship");
        stage.setResizable(false);
        GameStage.setStage(stage);
        Player.setPlayer();
        GameStage.getInstance().setScene("main/java/Menu.fxml");
        GameStage.getInstance().getStage().getScene().getWindow().setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
            ConnectionHandler.getInstance().closeConnection();
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

