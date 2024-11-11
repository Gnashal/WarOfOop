package com.warofoop.warofoop;

import com.warofoop.warofoop.controllers.GameController;
import com.warofoop.warofoop.controllers.LobbyController;
import com.warofoop.warofoop.controllers.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            SceneManager sceneManager = new SceneManager(stage);
            sceneManager.switchToMainMenu();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
