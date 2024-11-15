package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.Game;
import com.warofoop.warofoop.build.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.net.MalformedURLException;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;

public class LobbyController {
    private final String[] imagePaths = {
            "/Maps/DebugMap.png", // debug maps
            "/Maps/DebugMap2.png" // debug maps
    };



    @FXML
    private ListView<String> imageListView;

    @FXML
    private ImageView imageView;
    String selectedImageName;
    private Player player1;
    private Player player2;
    private Game game;
    int imageCount = 0;

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
    private boolean hasSelectedMap = false;

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
        if (player1Ready && player2Ready && hasSelectedMap) {
            statusLabel.setText("Both players are ready! Starting the game...");
            System.out.println("Both players ready, switching to game scene...");

            // Ga-initializeGame kog Player and Game classes here and to be passed to the Game Controller
            String name1 = playerName1.getText();
            String name2 = playerName2.getText();
            String MapName = imageListView.getSelectionModel().getSelectedItem();
            player1 = new Player(100f, 500, name1, 10);
            player2 = new Player(100f, 500, name2, 10);
            game = new Game(player1, player2, MapName);
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
        } else if (!hasSelectedMap) {
            statusLabel.setText("Select a Map...");
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
        sceneManager.switchToGame(game);
    }

    //    for maps
    @FXML
    public void loadPreloadedImages() {
        for (String path : imagePaths) {
            try {
                if (imageCount < imagePaths.length) {
                    String imagePath = getClass().getResource(path).toExternalForm();
                    imageListView.getItems().add(imagePath.substring(imagePath.lastIndexOf('/') + 1));
                    imageCount++;
                } else {
                    return;
                }
            } catch (NullPointerException e) {
                System.out.println("Error: Image path not found: " + path);
            }
        }
    }

    @FXML
    public void handleSelectImage() throws IOException {
        selectedImageName = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImageName != null) {
            boolean imageFound = false;
            for (String path : imagePaths) {
                URL resourceUrl = getClass().getResource(path);
                if (resourceUrl != null) {
                    File file = new File(resourceUrl.getPath());
                    if (file.getName().equals(selectedImageName)) {
                        try {
                            Image image = new Image(resourceUrl.toExternalForm());
                            imageView.setImage(image);
                            hasSelectedMap = true;
                            System.out.println("Selected image: " + selectedImageName);
                            p1p2_ready();
                            imageFound = true;
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                } else {
                    System.out.println("Error: Resource URL is null for path: " + path);
                }
            }
            if (!imageFound) {
                System.out.println("Warning: No matching image found for " + selectedImageName);
            }
        }
    }

}