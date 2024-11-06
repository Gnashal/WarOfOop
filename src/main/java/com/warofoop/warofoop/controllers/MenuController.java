package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
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
    Button exitButton ;

    public void switchToLobby(){
        playButton.setOnAction(e -> {
            try {
                new SceneManager(rootpane, "Lobby.fxml");
            } catch (IOException err) {
                throw new RuntimeException(err);
            }
        });
        System.out.println("Switched To Lobby");

    }

}