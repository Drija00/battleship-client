package main.java;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GameStage {
    private final Stage stage;
    private static GameStage instance;

    public GameStage(Stage stage) {
        this.stage = stage;
    }

    public synchronized static void setStage(Stage stage) {
        if(instance == null) {
            instance = new GameStage(stage);
        }
    }

    public static GameStage getInstance() {
        if(instance == null)
            throw new RuntimeException("Stage is not set");
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public void setScene(String FXMLResourcePath) {
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getClassLoader().getResource(FXMLResourcePath))));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            stage.show();
            setPosition();
        }
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
        setPosition();
    }


    private void setPosition() {
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
}

