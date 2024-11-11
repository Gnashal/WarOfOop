package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GameController {

    private SceneManager sceneManager;

    @FXML
    AnchorPane gamePane;

    @FXML
    Button backButton;

    @FXML
    private Label playerName1;

    @FXML
    private Label playerName2;

    @FXML
    private Label playerLabelEcon1;

    @FXML
    private Label playerLabelEcon2;

    public int playerHealth1 = 100;
    public int playerHealth2 = 100;

    public int playerEcon1 = 0;
    public int playerEcon2 = 0;
    public int roundCount = 1;

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

    public void initialize() {
        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();

        gamePane.setOnKeyPressed(this::hotKey);

        runEcon();
    }

    @FXML
    public void returnToPrevScene() throws IOException {
        if (sceneManager == null) {
            System.out.println("Scene Manager Null");
            return;
        }
        System.out.println("Switched to Lobby");
        sceneManager.switchToLobby();
    }

    @FXML
    protected void hotKey(KeyEvent event) {
        switch (event.getCode()) {
            case Q -> playerEcon1 -= 10;
            case W -> playerEcon1 -= 20;
            case E -> playerEcon1 -= 15;

            case I -> playerEcon2 -= 10;
            case O -> playerEcon2 -= 20;
            case P -> playerEcon2 -= 15;
        }

        playerLabelEcon1.setText(" " + playerEcon1);
        playerLabelEcon2.setText(" " + playerEcon2);
    }

    private void runEcon() {
        Task<Void> giveMoney = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (playerHealth1 > 0 && playerHealth2 > 0) {
                    playerEcon1 += 15;
                    playerEcon2 += 15;

                    Platform.runLater(() -> {
                        playerLabelEcon1.setText(" " + playerEcon1);
                        playerLabelEcon2.setText(" " + playerEcon2);
                    });

                    Thread.sleep(6000);
                }

                return null;
            }
        };

        new Thread(giveMoney).start();
    }
}
