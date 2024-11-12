package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.Game;
import com.warofoop.warofoop.build.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class LobbyController {

    private Player player1;
    private Player player2;
    private Game game;

    private SceneManager sceneManager;

    @FXML
    private AnchorPane lobbyPane;

    @FXML
    private Button backButton, player1Button, player2Button;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField playerName1, playerName2;

    private boolean player1Ready = false;
    private boolean player2Ready = false;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Game getGame() {
        return game;
    }

    // Set the SceneManager
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        System.out.println("SceneManager has been set in LobbyController.");
    }

    // Check if both players are ready and switch to the game scene
    private void p1p2_ready() throws IOException {
        if (player1Ready && player2Ready) {
            statusLabel.setText("Both players are ready! Starting the game...");
            System.out.println("Both players ready, switching to game scene...");

            // Ga-initializeGame kog Player and Game classes here and to be passed to the Game Controller
            String name1 = playerName1.getText();
            String name2 = playerName2.getText();
            player1 = new Player(100f, 500, name1, 10);
            player2 = new Player(100f, 500, name2, 10);
            game = new Game(player1, player2);
            goToGame();
        } else {
            updateStatusLabel();
        }
    }

    @FXML
    public void toggle_player1(ActionEvent event) throws IOException {
        player1Ready = !player1Ready;
        player1Button.setText(player1Ready ? "Unready" : "Ready!");
        System.out.println("Player 1 ready: " + player1Ready);
        p1p2_ready();
    }

    @FXML
    public void toggle_player2(ActionEvent event) throws IOException {
        player2Ready = !player2Ready;
        player2Button.setText(player2Ready ? "Unready" : "Ready!");
        System.out.println("Player 2 ready: " + player2Ready);
        p1p2_ready();
    }

    // Update status label based on which players are ready
    private void updateStatusLabel() {
        if (player1Ready && !player2Ready) {
            statusLabel.setText("Player 1 is ready. Waiting for Player 2...");
        } else if (!player1Ready && player2Ready) {
            statusLabel.setText("Player 2 is ready. Waiting for Player 1...");
        } else {
            statusLabel.setText("Waiting for both players to be ready...");
        }
    }

    @FXML
    public void returnToPrevScene() throws IOException {
        if (sceneManager == null) {
            System.out.println("SceneManager is null! Cannot switch to main menu.");
            return;
        }
        System.out.println("Switching to Main Menu...");
        sceneManager.switchToMainMenu();
    }

    @FXML
    public void goToGame() throws IOException {
        if (sceneManager == null) {
            System.out.println("SceneManager is null! Cannot switch to game scene.");
            return;
        }
        System.out.println("Switching to Game Scene...");
        game.startGame();
        sceneManager.switchToGame(player1, player2, game);
    }
}