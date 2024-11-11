package com.warofoop.warofoop.controllers;
import com.warofoop.warofoop.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.io.IOException;

public class LobbyController
{
    private final String[] imagePaths = {
            "/Maps/DebugMap.png", // debug maps
            "/Maps/DebugMap2.png" // debug maps
    };

    @FXML
    private ListView<String> imageListView;

    @FXML
    private ImageView imageView;

    @FXML
    AnchorPane lobbyPane;

    @FXML
    Button backButton;

    @FXML
    Button player1Button;

    @FXML
    Button player2Button;

    @FXML
    Label statusLabel;

    boolean player1Ready = false;
    boolean player2Ready = false;


    public void toggle_player1(javafx.event.ActionEvent actionEvent) throws IOException {
        if (!player1Ready) {
            player1Ready = true;
            player1Button.setText("Unready");
        } else {
            player1Ready = false;
            player1Button.setText("Ready!");
        }
        p1p2_ready();
    }

    public void toggle_player2(javafx.event.ActionEvent actionEvent) throws IOException {
        if (!player2Ready) {
            player2Ready = true;
            player2Button.setText("Unready");
        } else {
            player2Ready = false;
            player2Button.setText("Ready!");
        }
        p1p2_ready();
    }


    public void p1p2_ready() throws IOException {
        if (player1Ready && player2Ready) {
            statusLabel.setText("Both players are ready! Starting the game...");
            goToGame();
        } else {
            updateStatusLabel();
        }
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
        System.out.println("Switched to Main Menu");
        new SceneManager(lobbyPane, "Main_Menu.fxml");
    }

    //    for maps
    @FXML
    public void loadPreloadedImages() {
        for (String path : imagePaths) {
            try {
                String imagePath = getClass().getResource(path).toExternalForm();
                imageListView.getItems().add(imagePath.substring(imagePath.lastIndexOf('/') + 1));
            } catch (NullPointerException e) {
                System.out.println("Error: Image path not found: " + path);
            }
        }
    }

    @FXML
    public void handleSelectImage() {
        String selectedImageName = imageListView.getSelectionModel().getSelectedItem();
        if (selectedImageName != null) {
            for (String path : imagePaths) {
                File file = new File(getClass().getResource(path).getPath());
                if (file.getName().equals(selectedImageName)) {
                    try {
                        Image image = new Image(file.toURI().toURL().toString());
                        imageView.setImage(image);
                        System.out.println("Selected image: " + selectedImageName);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }


    @FXML
    public void goToGame() throws IOException {
        System.out.println("Entering Game!");
        new SceneManager(lobbyPane, "Game_Window.fxml");
    }

}
