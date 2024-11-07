package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LobbyController
{
    @FXML
    AnchorPane lobbyPane;

    @FXML
    Button backButton;

    @FXML
    public void returnToPrevScene() throws IOException {
        System.out.println("Switched to Main Menu");
        new SceneManager(lobbyPane, "Main_Menu.fxml");
    }
}
