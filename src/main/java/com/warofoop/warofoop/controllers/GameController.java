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

public class GameController {

    private SceneManager sceneManager;
    private Player player1;
    private Player player2;
    private Game game;

    private IntegerProperty deployTime = new SimpleIntegerProperty(20);
    private IntegerProperty startTime = new SimpleIntegerProperty(30);
    private IntegerProperty intervalTime = new SimpleIntegerProperty(3);
    private int roundCount = 1;

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
            sceneManager.reloadLobby();
        } else {
            System.out.println("Scene Manager not set!");
        }
    }

//    Cooldown Functions
    public void isArcherOnCD() {
        if (isArcherCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-15);

            isArcherCD = true;

            archerCDOver.setFill(Color.BLACK);
            archerCDOver.setOpacity(0.5);
            archerCDOver.setWidth(75.0);
            archerCDOver.setHeight(75.0);

            if (!archerStack.getChildren().contains(archerCDOver)) {
                archerStack.getChildren().add(archerCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(3), archerCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                archerStack.getChildren().remove(archerCDOver);
                isArcherCD = false;
            });

            coolDown.play();
        }


    }

    public void isFootmanOnCD() {
        if (isFootmanCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-10);

            isFootmanCD = true;

            footmanCDOver.setFill(Color.BLACK);
            footmanCDOver.setOpacity(0.5);
            footmanCDOver.setWidth(75.0);
            footmanCDOver.setHeight(75.0);

            if (!footmanStack.getChildren().contains(footmanCDOver)) {
                footmanStack.getChildren().add(footmanCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(5), footmanCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                footmanStack.getChildren().remove(footmanCDOver);
                isFootmanCD = false;
            });

            coolDown.play();
        }


    }

    public void isKnightOnCD() {
        if (isKnightCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-20);

            isKnightCD = true;

            knightCDOver.setFill(Color.BLACK);
            knightCDOver.setOpacity(0.5);
            knightCDOver.setWidth(75.0);
            knightCDOver.setHeight(75.0);

            if (!knightStack.getChildren().contains(knightCDOver)) {
                knightStack.getChildren().add(knightCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(10), knightCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                knightStack.getChildren().remove(knightCDOver);
                isKnightCD = false;
            });

            coolDown.play();
        }


    }

    public void isTrollOnCD() {
        if (isTrollCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-15);

            isTrollCD = true;

            trollCDOver.setFill(Color.BLACK);
            trollCDOver.setOpacity(0.5);
            trollCDOver.setWidth(75.0);
            trollCDOver.setHeight(75.0);

            if (!trollStack.getChildren().contains(trollCDOver)) {
                trollStack.getChildren().add(trollCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(3), trollCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                trollStack.getChildren().remove(trollCDOver);
                isTrollCD = false;
            });

            coolDown.play();
        }


    }

    public void isOrcOnCD() {
        if (isOrcCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-10);

            isOrcCD = true;

            orcCDOver.setFill(Color.BLACK);
            orcCDOver.setOpacity(0.5);
            orcCDOver.setWidth(75.0);
            orcCDOver.setHeight(75.0);

            if (!orcStack.getChildren().contains(orcCDOver)) {
                orcStack.getChildren().add(orcCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(5), orcCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                orcStack.getChildren().remove(orcCDOver);
                isOrcCD = false;
            });

            coolDown.play();
        }


    }

    public void isOgreOnCD() {
        if (isOgreCD) {
            return;
        }

        if (player1.getGold() > 0) {
            player1.changeGold(-20);

            isOgreCD = true;

            ogreCDOver.setFill(Color.BLACK);
            ogreCDOver.setOpacity(0.5);
            ogreCDOver.setWidth(75.0);
            ogreCDOver.setHeight(75.0);

            if (!ogreStack.getChildren().contains(ogreCDOver)) {
                ogreStack.getChildren().add(ogreCDOver);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(10), ogreCDOver);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                ogreStack.getChildren().remove(ogreCDOver);
                isOgreCD = false;
            });

            coolDown.play();
        }


    }

    public void isArcherOnCD2() {
        if (isArcherCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-15);

            isArcherCD2 = true;

            archerCDOver2.setFill(Color.BLACK);
            archerCDOver2.setOpacity(0.5);
            archerCDOver2.setWidth(75.0);
            archerCDOver2.setHeight(75.0);

            if (!archerStack2.getChildren().contains(archerCDOver2)) {
                archerStack2.getChildren().add(archerCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(3), archerCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                archerStack2.getChildren().remove(archerCDOver2);
                isArcherCD2 = false;
            });

            coolDown.play();
        }


    }

    public void isFootmanOnCD2() {
        if (isFootmanCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-10);

            isFootmanCD2 = true;

            footmanCDOver2.setFill(Color.BLACK);
            footmanCDOver2.setOpacity(0.5);
            footmanCDOver2.setWidth(75.0);
            footmanCDOver2.setHeight(75.0);

            if (!footmanStack2.getChildren().contains(footmanCDOver2)) {
                footmanStack2.getChildren().add(footmanCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(5), footmanCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                footmanStack2.getChildren().remove(footmanCDOver2);
                isFootmanCD2 = false;
            });

            coolDown.play();
        }


    }

    public void isKnightOnCD2() {
        if (isKnightCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-20);

            isKnightCD2 = true;
            knightCDOver2.setFill(Color.BLACK);
            knightCDOver2.setOpacity(0.5);
            knightCDOver2.setWidth(75.0);
            knightCDOver2.setHeight(75.0);

            if (!knightStack2.getChildren().contains(knightCDOver2)) {
                knightStack2.getChildren().add(knightCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(10), knightCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                knightStack2.getChildren().remove(knightCDOver2);
                isKnightCD2 = false;
            });

            coolDown.play();
        }


    }

    public void isTrollOnCD2() {
        if (isTrollCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-15);

            isTrollCD2 = true;

            trollCDOver2.setFill(Color.BLACK);
            trollCDOver2.setOpacity(0.5);
            trollCDOver2.setWidth(75.0);
            trollCDOver2.setHeight(75.0);

            if (!trollStack2.getChildren().contains(trollCDOver2)) {
                trollStack2.getChildren().add(trollCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(3), trollCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                trollStack2.getChildren().remove(trollCDOver2);
                isTrollCD2 = false;
            });

            coolDown.play();
        }


    }

    public void isOrcOnCD2() {
        if (isOrcCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-10);

            isOrcCD2 = true;

            orcCDOver2.setFill(Color.BLACK);
            orcCDOver2.setOpacity(0.5);
            orcCDOver2.setWidth(75.0);
            orcCDOver2.setHeight(75.0);

            if (!orcStack2.getChildren().contains(orcCDOver2)) {
                orcStack2.getChildren().add(orcCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(5), orcCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                orcStack2.getChildren().remove(orcCDOver2);
                isOrcCD2 = false;
            });

            coolDown.play();
        }


    }

    public void isOgreOnCD2() {
        if (isOgreCD2) {
            return;
        }

        if (player2.getGold() > 0) {
            player2.changeGold(-20);

            isOgreCD2 = true;

            ogreCDOver2.setFill(Color.BLACK);
            ogreCDOver2.setOpacity(0.5);
            ogreCDOver2.setWidth(75.0);
            ogreCDOver2.setHeight(75.0);

            if (!ogreStack2.getChildren().contains(ogreCDOver2)) {
                ogreStack2.getChildren().add(ogreCDOver2);
            }

            FadeTransition coolDown = new FadeTransition(Duration.seconds(10), ogreCDOver2);
            coolDown.setFromValue(0.5);
            coolDown.setToValue(0);
            coolDown.setOnFinished(event -> {
                ogreStack2.getChildren().remove(ogreCDOver2);
                isOgreCD2 = false;
            });

            coolDown.play();
        }



    }

//    ============================================================

    @FXML
    protected void hotKey(KeyEvent event) {
        switch (event.getCode()) {
            case Q -> {

                isFootmanOnCD();
            }
            case W -> {

                isKnightOnCD();
            }
            case E -> {

                isArcherOnCD();
            }
            case A -> {

                isOrcOnCD();
            }
            case S -> {

                isOgreOnCD();
            }
            case D -> {

                isTrollOnCD();
            }
            case I -> {

                isFootmanOnCD2();
            }
            case O -> {

                isKnightOnCD2();
            }
            case P -> {

                isArcherOnCD2();
            }
            case J -> {

                isOrcOnCD2();
            }
            case K -> {

                isOgreOnCD2();
            }
            case L -> {

                isTrollOnCD2();
            }

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
            if (player1.isDefeated()) {
                System.out.println(player2.getName() + " won!");
                gameLoop.stop();
//                gamePane.setEffect(blurEffect); //BLUR
                gamePane.setStyle("-fx-background-color: black;");
                gameText.setText(player2.getName() + " Won!");
                applyZoomInAndFadeOutEffect(gameText);
            } else if (player2.isDefeated()) {
                System.out.println(player1.getName() + " won!");
                gameLoop.stop();
//                gamePane.setEffect(blurEffect); //BLUR
                gamePane.setStyle("-fx-background-color: black;");
                gameText.setText(player1.getName() + " Won!");
                applyZoomInAndFadeOutEffect(gameText);

            }
        });
    }
}

