package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;

import java.net.URL;


import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GameController {

    private SceneManager sceneManager;
    private Player player1;
    private Player player2;
    private Game game;
    private String map;
    public float playerHealth1;
    public float playerHealth2;

    public int playerEcon1;
    public int playerEcon2;
    public int roundCount;

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


    public void setSceneManager(SceneManager sceneManager) {
        if (this.sceneManager == null) {
            System.out.println("Setting SceneManager for the first time");
        } else {
            System.out.println("SceneManager already set");
        }
        System.out.println("Scene Manager set");
        this.sceneManager = sceneManager;
    }



    public void setGame(Game newGame) throws IOException {
        this.player1 = newGame.getPlayer1();
        this.player2 = newGame.getPlayer2();
        this.game = newGame;
        this.map = newGame.getMap();
        System.out.println(player1.getName() + " " + player2.getName());
        System.out.println(map);
        initializeGame();
    }

    public void setGameBackground() {
        try {
            String mapPath = "/Maps/" + map;
            URL mapUrl = getClass().getResource(mapPath);
            if (mapUrl == null) {
                System.out.println("Map file not found at: " + mapPath);
                return;
            }
            Image mapImage = new Image(mapUrl.toExternalForm());
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            gamePane.setBackground(new Background(new BackgroundImage( mapImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    backgroundSize)));
            System.out.println("New Background Image set");
        } catch (NullPointerException e) {
            System.out.println("Error here:" + e);
            return;
        }

    }

    public void initializeGame() throws IOException {
        playerHealth1 = player1.getCurrhealth();
        playerHealth2 = player2.getCurrhealth();
        playerEcon1 = player1.getGold();
        playerEcon2 = player2.getGold();
        playerName1.setText(player1.getName());
        playerName2.setText(player2.getName());

        gamePane.setFocusTraversable(true);
        gamePane.requestFocus();
        setGameBackground();
        gamePane.setOnKeyPressed(this::hotKey);

        healthbar(playerHealthDisplay1, player1.getCurrhealth());
        healthbar(playerHealthDisplay2, player1.getCurrhealth());
        game.startGame();
        runEcon();
    }


    public void healthbar(ProgressBar bar, float health) {
        bar.setProgress(health / player1.getMaxhealth());

        String color;

        if (health > 70f) {
            color = "#4caf50";
        } else if (health > 30f) {
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
        System.out.println("Switched to Game");
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

    private void updateUI() {
//       TODO: THIS IS WHERE WE PUT UI UPDATES. Like Changing the round when the
//        TIMER IS UP, THE HEALTHBAR, ECONOMY ETC ETC
    }

    public void startGameLoop() {
        game.startGame();
        Task<Void> gameLoop = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (!game.isGameOver()) {
                    Platform.runLater(() -> {
                        updateUI();
                    });
                    game.updateGameLogic();
                    Thread.sleep(1000 / 60);
                }
                return null;
            }
        };
        new Thread(gameLoop).start();
    }

}
