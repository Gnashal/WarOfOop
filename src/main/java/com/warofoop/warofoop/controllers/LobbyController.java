package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LobbyController {

    private SceneManager sceneManager;

    @FXML
    AnchorPane lobbyPane;

    @FXML
    Button backButton;

    // Set SceneManager in the controller
    public void setSceneManager(SceneManager sceneManager) {
        if (this.sceneManager == null) {
            System.out.println("Setting SceneManager for the first time");
        } else {
            System.out.println("SceneManager already set");
        }
        System.out.println("Scene Manager set");
        this.sceneManager = sceneManager;
    }

    @FXML
    public void returnToPrevScene() throws IOException {
        if (sceneManager == null) {
            System.out.println("Scene Manager Null");
            return;
        }
        System.out.println("Switched to Main Menu");
        sceneManager.switchToMainMenu();
    }

    @FXML
    public void goToGame() throws IOException {
        if (sceneManager == null) {
            System.out.println("Scene Manager Null");
            return;
        }
        System.out.println("Entering Game!");
        sceneManager.switchToGame();
    }
}
