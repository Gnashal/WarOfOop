package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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

    @FXML
    private ProgressBar playerHealthDisplay1;

    @FXML
    private ProgressBar playerHealthDisplay2;

    public int playerHealth1 = 100;
    public int playerHealth2 = 100;

    public int playerEcon1 = 0;
    public int playerEcon2 = 0;
    public int roundCount = 1;

    public void initialize() {
        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();

        gamePane.setOnKeyPressed(this::hotKey);

        healthbar(playerHealthDisplay1, playerHealth1);
        healthbar(playerHealthDisplay2, playerHealth2);

        runEcon();
    }


    public void healthbar(ProgressBar bar, int health) {
        bar.setProgress(health / 100.0);

        String color;

        if (health > 70) {
            color = "#4caf50";
        } else if (health > 30) {
            color = "#ffeb3b";
        } else {
            color = "#f44336";
        }

        bar.setStyle(" -fx-accent: "+ color +";");
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

            case Z -> playerHealth1 -= 10;
            case X -> playerHealth2 -= 10;
        }

        playerHealth1 = Math.max(playerHealth1, 0);
        playerHealth2 = Math.max(playerHealth2, 0);

        healthbar(playerHealthDisplay1, playerHealth1);
        healthbar(playerHealthDisplay2, playerHealth2);

        playerLabelEcon1.setText(" "+ playerEcon1);
        playerLabelEcon2.setText(" "+ playerEcon2);

        if (playerHealth1 <= 0 || playerHealth2 <= 0) {
            Platform.runLater(() -> {
                if (playerHealth1 > 0) {
                    System.out.println(playerName1+ ", Won!");
                }

                if (playerHealth2 > 0) {
                    System.out.println(playerName2+ ", Won!");
                }
            });
        }
    }

    private void runEcon() {
        Task<Void> giveMoney = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (playerHealth1 > 0 && playerHealth2 > 0) {
                    playerEcon1 += 15;
                    playerEcon2 += 15;

                    Platform.runLater(() -> {
                        playerLabelEcon1.setText(" "+ playerEcon1);
                        playerLabelEcon2.setText(" "+ playerEcon2);
                    });

                    Thread.sleep(6000);
                }

                return null;
            }
        };

        new Thread(giveMoney).start();
    }

}
