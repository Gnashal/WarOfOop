package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.Game;
import com.warofoop.warofoop.build.Player;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class GameController {

    private SceneManager sceneManager;
    private Player player1;
    private Player player2;
    private Game game;

    private IntegerProperty deployTime = new SimpleIntegerProperty(20);
    private IntegerProperty startTime = new SimpleIntegerProperty(30);
    private IntegerProperty intervalTime = new SimpleIntegerProperty(3);
    private int roundCount = 1;

    @FXML
    private AnchorPane gamePane;
    @FXML
    private Button backButton;
    @FXML
    private Label playerName1, playerName2, playerLabelEcon1, playerLabelEcon2, timeLabel, roundLabel;
    @FXML
    private ProgressBar playerHealthDisplay1, playerHealthDisplay2;

    @FXML
    public void initialize() {
        roundLabel.setText("Round: " + roundCount);

        // Timeline for game logic updates
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), this::updateGameLogic)
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateGameLogic(ActionEvent event) {
        if (game.isGameOver()) {
            return;
        }

        if (deployTime.get() > 0) {
            deployTime.set(deployTime.get() - 1);
        } else if (intervalTime.get() > 0) {
            intervalTime.set(intervalTime.get() - 1);
        } else if (startTime.get() > 0) {
            startTime.set(startTime.get() - 1);
        } else {
            nextRound();
        }

        updateUI();
    }

    private void nextRound() {
        roundCount++;
        deployTime.set(20); // Selection Phase
        intervalTime.set(3); // Interval
        startTime.set(30); // Attacking Phase / Spawning Phase
        roundLabel.setText("Round: " + roundCount);
        System.out.println("Round " + roundCount + " begins!");
    }

    private void updateUI() {
        updateTimeLabel();
        updateHealthBars();
        updateEconomyLabels();
    }

    private void updateTimeLabel() {
        if (deployTime.get() > 0) {
            timeLabel.setText(String.format("%02d", deployTime.get()));
        } else if (intervalTime.get() > 0) {
            timeLabel.setText(String.format("%02d", intervalTime.get()));
        } else if (startTime.get() > 0) {
            timeLabel.setText(String.format("%02d", startTime.get()));
        }
    }

    private void updateHealthBars() {
        updateHealthBar(playerHealthDisplay1, player1.getCurrhealth());
        updateHealthBar(playerHealthDisplay2, player2.getCurrhealth());
    }

    private void updateHealthBar(ProgressBar bar, float health) {
        bar.setProgress(health / player1.getMaxhealth());

        String color = health > 70 ? "#4caf50" : (health > 30 ? "#ffeb3b" : "#f44336");
        bar.setStyle("-fx-accent: " + color + ";");
    }

    private void updateEconomyLabels() {
        playerLabelEcon1.setText(String.valueOf(player1.getGold()));
        playerLabelEcon2.setText(String.valueOf(player2.getGold()));
    }

    public void setSceneManager(SceneManager sceneManager) {
        if (this.sceneManager == null) {
            System.out.println("Setting SceneManager in Game Controller");
            this.sceneManager = sceneManager;
        }
    }

    public void setGame(Game newGame) throws IOException {
        this.player1 = newGame.getPlayer1();
        this.player2 = newGame.getPlayer2();
        this.game = newGame;

        initializeGame();
    }

    private void initializeGame() {
        roundCount = 1;
        playerName1.setText(player1.getName());
        playerName2.setText(player2.getName());
        updateHealthBars();
        updateEconomyLabels();
        setGameBackground();
    }

    private void setGameBackground() {
        try {
            String mapPath = "/Maps/" + game.getMap();
            URL mapUrl = getClass().getResource(mapPath);
            if (mapUrl == null) {
                System.out.println("Map file not found at: " + mapPath);
                return;
            }

            Image mapImage = new Image(mapUrl.toExternalForm());
            BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
            gamePane.setBackground(new Background(new BackgroundImage(
                    mapImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize
            )));
        } catch (Exception e) {
            System.out.println("Error setting background: " + e.getMessage());
        }
    }

    @FXML
    public void returnToPrevScene() throws IOException {
        if (sceneManager != null) {
            game.endGame();
            sceneManager.switchToLobby();
        } else {
            System.out.println("Scene Manager not set!");
        }
    }

    @FXML
    protected void hotKey(KeyEvent event) {
        switch (event.getCode()) {
            case Q -> player1.changeGold(-10);
            case W -> player1.changeGold(-20);
            case E -> player1.changeGold(-15);

            case I -> player2.changeGold(-10);
            case O -> player2.changeGold(-20);
            case P -> player2.changeGold(-15);

            case Z -> player1.changeHealth(-10);
            case X -> player2.changeHealth(-10);
        }

        player1.validateHealth();
        player2.validateHealth();
        updateUI();

        if (player1.isDefeated() || player2.isDefeated()) {
            handleGameOver();
        }
    }

    private void handleGameOver() {
        Platform.runLater(() -> {
            if (player1.isDefeated()) {
                System.out.println(player2.getName() + " won!");
            } else if (player2.isDefeated()) {
                System.out.println(player1.getName() + " won!");
            }
        });
    }
}

