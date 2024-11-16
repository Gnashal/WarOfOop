package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.*;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.net.URL;


import java.io.IOException;

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

    public boolean isArcherCD = false;
    public boolean isFootmanCD = false;
    public boolean isKnightCD = false;

    public boolean isTrollCD = false;
    public boolean isOrcCD = false;
    public boolean isOrgeCD = false;

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

    @FXML
    ImageView archerIcon;

    @FXML
    ImageView footmanIcon;

    @FXML
    ImageView knightIcon;

    @FXML
    ImageView trollIcon;

    @FXML
    ImageView orcIcon;

    @FXML
    ImageView ogreIcon;

    @FXML
    private StackPane archerStack;

    @FXML
    private StackPane footmanStack;

    @FXML
    private StackPane knightStack;

    @FXML
    private StackPane trollStack;

    @FXML
    private StackPane orcStack;

    @FXML
    private StackPane ogreStack;

    @FXML
    private Rectangle archerCDOver;

    @FXML
    private Rectangle footmanCDOver;

    @FXML
    private Rectangle knightCDOver;

    @FXML
    private Rectangle trollCDOver;

    @FXML
    private Rectangle orcCDOver;

    @FXML
    private Rectangle ogreCDOver;



    public void setSceneManager(SceneManager sceneManager) {
        if (this.sceneManager == null) {
            System.out.println("Setting SceneManager for the first time");
        } else {
            System.out.println("SceneManager already set");
        }
        System.out.println("Scene Manager set");
        this.sceneManager = sceneManager;
    }


// instance the game like setting shit to zero value not null
    public void setGame(Game newGame) throws IOException {
        this.player1 = newGame.getPlayer1();
        this.player2 = newGame.getPlayer2();
        this.game = newGame;
        this.map = newGame.getMap();
        System.out.println(player1.getName() + " " + player2.getName());
        System.out.println(map);
        initializeGame();
    }

//    Just the background setting image
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

//    get the stats from the player class
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


//    Setting the health bar color according to the health values
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

//    Returns to previous scene or window
    @FXML
    public void returnToPrevScene() throws IOException {
        if (sceneManager == null) {
            System.out.println("Scene Manager Null");
            return;
        }
        System.out.println("Switched to Game");
        sceneManager.switchToLobby();
    }

    public void isArcherOnCD() {
        if (isArcherCD) {
            return;
        }

        isArcherCD = true;

        archerCDOver.setFill(Color.BLACK);
        archerCDOver.setOpacity(0.5);
        archerCDOver.setWidth(100.0);
        archerCDOver.setHeight(100.0);

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

    public void isFootmanOnCD() {
        if (isFootmanCD) {
            return;
        }

        isFootmanCD = true;

        footmanCDOver.setFill(Color.BLACK);
        footmanCDOver.setOpacity(0.5);

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

    public void isKnightOnCD() {
        if (isKnightCD) {
            return;
        }

        isKnightCD = true;
        knightCDOver.setFill(Color.BLACK);
        knightCDOver.setOpacity(0.5);

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

    public void isTrollOnCD() {
        if (isTrollCD) {
            return;
        }

        isTrollCD = true;

        trollCDOver.setFill(Color.BLACK);
        trollCDOver.setOpacity(0.5);

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

    public void isOrcOnCD() {
        if (isOrcCD) {
            return;
        }

        isOrcCD = true;

        orcCDOver.setFill(Color.BLACK);
        orcCDOver.setOpacity(0.5);

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

    public void isOrgeOnCD() {
        if (isOrgeCD) {
            return;
        }

        isOrgeCD = true;

        ogreCDOver.setFill(Color.BLACK);
        ogreCDOver.setOpacity(0.5);

        if (!ogreStack.getChildren().contains(ogreCDOver)) {
            ogreStack.getChildren().add(ogreCDOver);
        }

        FadeTransition coolDown = new FadeTransition(Duration.seconds(10), ogreCDOver);
        coolDown.setFromValue(0.5);
        coolDown.setToValue(0);
        coolDown.setOnFinished(event -> {
            ogreStack.getChildren().remove(ogreCDOver);
            isOrgeCD = false;
        });

        coolDown.play();
    }

//    V's Feature Testing
    @FXML
    protected void hotKey(KeyEvent event) {
        switch (event.getCode()) {
            case Q -> {
                playerEcon1 -= 10;
                isFootmanOnCD();
            }
            case W -> {
                playerEcon1 -= 20;
                isKnightOnCD();
            }
            case E -> {
                playerEcon1 -= 15;
                isArcherOnCD();
            }

            case I -> {
                playerEcon2 -= 10;
                isOrcOnCD();
            }
            case O -> {
                playerEcon2 -= 20;
                isOrgeOnCD();
            }
            case P -> {
                playerEcon2 -= 15;
                isTrollOnCD();
            }

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

//    Asynch function to run the economy within the game
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

//    Send data to game class
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
