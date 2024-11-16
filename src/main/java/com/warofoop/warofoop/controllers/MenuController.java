package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {

    private SceneManager sceneManager;

    @FXML
    private AnchorPane rootpane;

    @FXML
    Button playButton;

    @FXML
    Button exitButton;

    // Setter method to inject SceneManager
    public void setSceneManager(SceneManager sceneManager) {
        if (this.sceneManager == null) {
            System.out.println("Setting SceneManager in Menu Controller");
        } else {
            System.out.println("SceneManager already set");
        }
        System.out.println("Scene Manager set");
        this.sceneManager = sceneManager;
    }

    // Switch to Game
    @FXML
    public void switchToLobby(ActionEvent event) throws IOException {
        if (sceneManager == null) {
            System.out.println("Scene Manager Null");
            return;
        }
        sceneManager.switchToLobby();
        System.out.println("Switched To Game");
    }

    // Exit the game
    @FXML
    public void exitGame() {
        try {
            System.out.println("Exiting Application..");
            Platform.exit();
            System.out.println("Exit Successful");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
