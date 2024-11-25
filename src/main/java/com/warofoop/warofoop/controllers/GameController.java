package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.Game;
import com.warofoop.warofoop.build.Player;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GameController {
    private final String[] playerBaseImagePaths = {
            "/assets/Player1Castle/Player1FullHp.png",
            "/assets/Player1Castle/Player1HalfHp.png",
            "/assets/Player1Castle/Player1NoHp.png",
            "/assets/Player2Castle/Player2FullHP.png",
            "/assets/Player2Castle/Player2MidHP.png",
            "/assets/Player2Castle/Player2NoHP.png"
    };

    @FXML
    private ListView<String> imageListView;
    String selectedImage;

    @FXML
    private ImageView player1Castle;

    @FXML
    private ImageView player2Castle;

    private SceneManager sceneManager;
    private Player player1;
    private Player player2;
    private Game game;

    private final IntegerProperty deployTime = new SimpleIntegerProperty(20);
    private final IntegerProperty startTime = new SimpleIntegerProperty(30);
    private final IntegerProperty intervalTime = new SimpleIntegerProperty(3);
    private int roundCount;

    public boolean isArcherCD = false;
    public boolean isFootmanCD = false;
    public boolean isKnightCD = false;

    public boolean isTrollCD = false;
    public boolean isOrcCD = false;
    public boolean isOgreCD = false;

    public boolean isArcherCD2 = false;
    public boolean isFootmanCD2 = false;
    public boolean isKnightCD2 = false;

    public boolean isTrollCD2 = false;
    public boolean isOrcCD2 = false;
    public boolean isOgreCD2 = false;


    private boolean isPaused = false;

    @FXML
    private AnchorPane gamePane;
    @FXML
    private Button backButton;
    @FXML
    private Label playerName1, playerName2, playerLabelEcon1, playerLabelEcon2, timeLabel, roundLabel;
    @FXML
    private ProgressBar playerHealthDisplay1, playerHealthDisplay2;

//  This is for the unit control UI panel
    @FXML
    ImageView archerIcon, footmanIcon, knightIcon, trollIcon, orcIcon, ogreIcon;

    @FXML
    private StackPane archerStack, footmanStack, knightStack, trollStack, orcStack, ogreStack;

    @FXML
    private Rectangle archerCDOver, footmanCDOver, knightCDOver, trollCDOver, orcCDOver, ogreCDOver;

    @FXML
    ImageView archerIcon2, footmanIcon2, knightIcon2, trollIcon2, orcIcon2, ogreIcon2;

    @FXML
    private StackPane archerStack2, footmanStack2, knightStack2, trollStack2, orcStack2, ogreStack2;

    @FXML
    private Rectangle archerCDOver2, footmanCDOver2, knightCDOver2, trollCDOver2, orcCDOver2, ogreCDOver2;
//  ==================================================================================================

    public Timeline gameLoop;
    //PLEASE USE THIS FOR THE GAMETIME LINE AS A WHOLE
    //USED FOR PAUSING A GAME && RESUMING A GAME

    @FXML
    private Region overlay;

    @FXML
    private Label gameText;

    @FXML
    private GaussianBlur blurEffect = new GaussianBlur();



    @FXML
    public void initialize() {
        gameLoop = new Timeline(
                new KeyFrame(Duration.seconds(1), this::updateGameLogic)
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void updateGameLogic(ActionEvent event) {
        if (!game.isGameOver()) {

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
        } else {
            handleGameOver();
        }
    }

    private void nextRound() {
        roundCount++;
        deployTime.set(game.getDeploy_time()); // Selection Phase
        intervalTime.set(game.getInterval_time()); // Interval
        startTime.set(game.getStart_time()); // Attacking Phase / Spawning Phase
        roundLabel.setText("Round: " + roundCount);
        System.out.println("Round " + roundCount + " begins!");
    }

    private void updateUI() {
        gameText.setText("Round: " + roundCount);
        applyZoomInAndFadeOutEffect(gameText);
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
        baseStateOnHP();
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
        roundCount = game.getRoundCount();
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
            if (!game.isGameOver()) {
                game.endGame();
            }
            sceneManager.reloadLobby();
        } else {
            System.out.println("Scene Manager not set!");
        }
    }

//    Cooldown Functions
public void handleCooldown(
        Player player,
        int goldCost,
        double cooldownDuration,
        boolean isOnCooldown,
        Runnable setCooldownFlag,
        Runnable clearCooldownFlag,
        StackPane stackPane,
        Rectangle cooldownOverlay
) {
    if (isOnCooldown || player.getGold() < goldCost) {
        return;
    }

    player.changeGold(-goldCost);
    setCooldownFlag.run();

    cooldownOverlay.setFill(Color.BLACK);
    cooldownOverlay.setOpacity(0.5);
    cooldownOverlay.setWidth(75.0);
    cooldownOverlay.setHeight(75.0);

    if (!stackPane.getChildren().contains(cooldownOverlay)) {
        stackPane.getChildren().add(cooldownOverlay);
    }

    FadeTransition coolDown = new FadeTransition(Duration.seconds(cooldownDuration), cooldownOverlay);
    coolDown.setFromValue(0.5);
    coolDown.setToValue(0);
    coolDown.setOnFinished(event -> {
        stackPane.getChildren().remove(cooldownOverlay);
        clearCooldownFlag.run();
    });

    coolDown.play();
}
    public void isArcherOnCD() {
        handleCooldown(player1, 15, 3.0, isArcherCD,
                () -> isArcherCD = true,
                () -> isArcherCD = false,
                archerStack, archerCDOver);
    }

    public void isFootmanOnCD() {
        handleCooldown(player1, 10, 5.0, isFootmanCD,
                () -> isFootmanCD = true,
                () -> isFootmanCD = false,
                footmanStack, footmanCDOver);
    }

    public void isKnightOnCD() {
        handleCooldown(player1, 20, 10.0, isKnightCD,
                () -> isKnightCD = true,
                () -> isKnightCD = false,
                knightStack, knightCDOver);
    }

    public void isTrollOnCD() {
        handleCooldown(player1, 15, 3.0, isTrollCD,
                () -> isTrollCD = true,
                () -> isTrollCD = false,
                trollStack, trollCDOver);
    }

    public void isOrcOnCD() {
        handleCooldown(player1, 10, 5.0, isOrcCD,
                () -> isOrcCD = true,
                () -> isOrcCD = false,
                orcStack, orcCDOver);
    }

    public void isOgreOnCD() {
        handleCooldown(player1, 20, 10.0, isOgreCD,
                () -> isOgreCD = true,
                () -> isOgreCD = false,
                ogreStack, ogreCDOver);
    }

    public void isArcherOnCD2() {
        handleCooldown(player2, 15, 3.0, isArcherCD2,
                () -> isArcherCD2 = true,
                () -> isArcherCD2 = false,
                archerStack2, archerCDOver2);
    }

    public void isFootmanOnCD2() {
        handleCooldown(player2, 10, 5.0, isFootmanCD2,
                () -> isFootmanCD2 = true,
                () -> isFootmanCD2 = false,
                footmanStack2, footmanCDOver2);
    }

    public void isKnightOnCD2() {
        handleCooldown(player2, 20, 10.0, isKnightCD2,
                () -> isKnightCD2 = true,
                () -> isKnightCD2 = false,
                knightStack2, knightCDOver2);
    }

    public void isTrollOnCD2() {
        handleCooldown(player2, 15, 3.0, isTrollCD2,
                () -> isTrollCD2 = true,
                () -> isTrollCD2 = false,
                trollStack2, trollCDOver2);
    }

    public void isOrcOnCD2() {
        handleCooldown(player2, 10, 5.0, isOrcCD2,
                () -> isOrcCD2 = true,
                () -> isOrcCD2 = false,
                orcStack2, orcCDOver2);
    }

    public void isOgreOnCD2() {
        handleCooldown(player2, 20, 10.0, isOgreCD2,
                () -> isOgreCD2 = true,
                () -> isOgreCD2 = false,
                ogreStack2, ogreCDOver2);
    }


//    ============================================================

    @FXML
    protected void hotKey(KeyEvent event) {
        switch (event.getCode()) {
            case E -> {

                isFootmanOnCD();
            }
            case W -> {

                isKnightOnCD();
            }
            case Q -> {

                isArcherOnCD();
            }
            case D -> {

                isOrcOnCD();
            }
            case S -> {

                isOgreOnCD();
            }
            case A -> {

                isTrollOnCD();
            }
            case P -> {

                isFootmanOnCD2();
            }
            case O -> {

                isKnightOnCD2();
            }
            case I -> {

                isArcherOnCD2();
            }
            case L -> {

                isOrcOnCD2();
            }
            case K -> {

                isOgreOnCD2();
            }
            case J -> {

                isTrollOnCD2();
            }

            case Z -> player1.changeHealth(-10);
            case X -> player2.changeHealth(-10);
        }

        player1.validateHealth();
        player2.validateHealth();
        updateUI();

    }

    public void applyZoomInAndFadeOutEffect(Label gameLabel) {
        // Scale transition (zoom-in effect for the label)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), gameLabel);
        scaleTransition.setToX(1.2);  // Zoom-in on X axis (horizontal scaling)
        scaleTransition.setToY(1.2);  // Zoom-in on Y axis (vertical scaling)

        // Fade transition (fade-out effect)
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), gameLabel);
        fadeTransition.setToValue(0);  // Fade out to 0 opacity

        // Play the transitions together
        scaleTransition.play();
        fadeTransition.play();
    }

    private void handleGameOver() {
        Platform.runLater(() -> {
            String winner = (player1.isDefeated()) ? player2.getName(): player1.getName();
                System.out.println(winner + " won!");
                gameLoop.stop();
                gamePane.setStyle("-fx-background-color: gold;");
                gameText.setOpacity(1);
                gameText.setText(winner + " Won!");
                applyZoomInAndFadeOutEffect(gameText);
                game.endGame();
        });
    }

    @FXML
    public void baseStateOnHP() {
        if (player1.getCurrhealth() > 70f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[0])).toString()));
        } else if (player1.getCurrhealth() > 35f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[1])).toString()));
        } else if (player1.getCurrhealth() > 10f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[2])).toString()));
        }
        if (player2.getCurrhealth() > 70f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[3])).toString()));
        } else if (player2.getCurrhealth() > 35f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[4])).toString()));
        } else if (player2.getCurrhealth() > 10f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[5])).toString()));
        }
    }
}

