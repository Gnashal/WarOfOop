package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {
    @FXML
    AnchorPane rootpane;

    @FXML
    Button playButton;

    @FXML
    Button exitButton;

    public void switchToLobby() throws IOException {
        new SceneManager(rootpane, "Lobby_Window.fxml");
        System.out.println("Switched To Lobby");
    }

    public void exitGame() {
        Platform.exit();
        System.out.println("Exit Successful");
    }

}