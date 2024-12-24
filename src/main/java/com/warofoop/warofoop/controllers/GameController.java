package com.warofoop.warofoop.controllers;

import com.warofoop.warofoop.SceneManager;
import com.warofoop.warofoop.build.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameController {
    private final String[] playerBaseImagePaths = {
            "/assets/Player1Castle/Player1FullHp.png",
            "/assets/Player1Castle/Player1HalfHp.png",
            "/assets/Player1Castle/Player1NoHp.png",
            "/assets/Player2Castle/Player2FullHP.png",
            "/assets/Player2Castle/Player2MidHP.png",
            "/assets/Player2Castle/Player2NoHP.png"
    };

    private List<Unit> player1Units = new ArrayList<>();
    private List<Unit> player2Units = new ArrayList<>();

    private Unit unit;
    private Archer archer;
    private Footman footman;
    private Goblin goblin;
    private Knight knight;
    private Ogre ogre;
    private Troll troll;

    @FXML
    private ImageView player1Castle;

    @FXML
    private ImageView player2Castle;

    @FXML
    private Label currCap1;
    @FXML
    private Label currCap2;

    @FXML
    private Label player1UnitCap;
    @FXML
    private Label player2UnitCap;

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
        updateUnitCap();
    }

    //    wala pa na human
    private void updateUnitCap() {
        if (player1.getCurrCap() < player1.getThreshold()) {
            currCap1.setText(String.valueOf(player1.getCurrCap()));
        }
        if (player2.getCurrCap() < player2.getThreshold()) {
            currCap2.setText(String.valueOf(player2.getCurrCap()));
        }
    }

    private void setMaxCap() {
        player1UnitCap.setText(String.valueOf(player1.getThreshold()));
        player2UnitCap.setText(String.valueOf(player1.getThreshold()));
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
        setMaxCap();
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
            String unitName,
            int goldCost,
            double cooldownDuration,
            boolean isOnCooldown,
            Runnable setCooldownFlag,
            Runnable clearCooldownFlag,
            StackPane stackPane,
            Rectangle cooldownOverlay,
            int unitSize
    ) {
        if (isOnCooldown || player.getGold() < goldCost || player.getCurrCap() > player.getThreshold()) {
            return;
        }

        Random random = new Random();
        if(player == player1) {
            int randomXPlayer1 = 350 + random.nextInt(101);
            int randomYPlayer1 = 400 + random.nextInt(401);
            spawnUnit(unitName, player1, randomXPlayer1, randomYPlayer1);
        }else{
            int randomXPlayer2 = 1530 - random.nextInt(101);
            int randomYPlayer2 = 400 + random.nextInt(401);
            spawnUnit(unitName, player2, randomXPlayer2, randomYPlayer2);
        }

        player.changeGold(-goldCost);
        player.changeCurrCap(unitSize);
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
        handleCooldown(player1, "Archer", 20, 3.0, isArcherCD,
                () -> isArcherCD = true,
                () -> isArcherCD = false,
                archerStack, archerCDOver, 5);
    }

    public void isFootmanOnCD() {
        handleCooldown(player1, "Footman", 18, 5.0, isFootmanCD,
                () -> isFootmanCD = true,
                () -> isFootmanCD = false,
                footmanStack, footmanCDOver, 4);
    }

    public void isKnightOnCD() {
        handleCooldown(player1, "Knight", 35, 10.0, isKnightCD,
                () -> isKnightCD = true,
                () -> isKnightCD = false,
                knightStack, knightCDOver, 11);
    }

    public void isTrollOnCD() {
        handleCooldown(player1, "Troll", 14, 3.0, isTrollCD,
                () -> isTrollCD = true,
                () -> isTrollCD = false,
                trollStack, trollCDOver, 5);
    }

    public void isOrcOnCD() {
        handleCooldown(player1, "Goblin", 8, 5.0, isOrcCD,
                () -> isOrcCD = true,
                () -> isOrcCD = false,
                orcStack, orcCDOver, 3);
    }

    public void isOgreOnCD() {
        handleCooldown(player1, "Ogre", 35, 10.0, isOgreCD,
                () -> isOgreCD = true,
                () -> isOgreCD = false,
                ogreStack, ogreCDOver, 11);
    }

    public void isArcherOnCD2() {
        handleCooldown(player2, "Archer", 20, 3.0, isArcherCD2,
                () -> isArcherCD2 = true,
                () -> isArcherCD2 = false,
                archerStack2, archerCDOver2, 5);
    }

    public void isFootmanOnCD2() {
        handleCooldown(player2, "Footman", 18, 5.0, isFootmanCD2,
                () -> isFootmanCD2 = true,
                () -> isFootmanCD2 = false,
                footmanStack2, footmanCDOver2, 4);
    }

    public void isKnightOnCD2() {
        handleCooldown(player2, "Knight", 35, 10.0, isKnightCD2,
                () -> isKnightCD2 = true,
                () -> isKnightCD2 = false,
                knightStack2, knightCDOver2, 11);
    }

    public void isTrollOnCD2() {
        handleCooldown(player2, "Troll", 14, 3.0, isTrollCD2,
                () -> isTrollCD2 = true,
                () -> isTrollCD2 = false,
                trollStack2, trollCDOver2, 5);
    }

    public void isOrcOnCD2() {
        handleCooldown(player2, "Goblin", 8, 5.0, isOrcCD2,
                () -> isOrcCD2 = true,
                () -> isOrcCD2 = false,
                orcStack2, orcCDOver2, 3);
    }

    public void isOgreOnCD2() {
        handleCooldown(player2, "Ogre", 35, 10.0, isOgreCD2,
                () -> isOgreCD2 = true,
                () -> isOgreCD2 = false,
                ogreStack2, ogreCDOver2, 11);
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
            String winner = (player1.isDefeated()) ? player2.getName() : player1.getName();
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
        if (player1.getCurrhealth() > 45f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[0])).toString()));
        } else if (player1.getCurrhealth() > 20f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[1])).toString()));
        } else if (player1.getCurrhealth() <= 0f) {
            player1Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[2])).toString()));
        }
        if (player2.getCurrhealth() > 45f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[3])).toString()));
        } else if (player2.getCurrhealth() > 20f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[4])).toString()));
        } else if (player2.getCurrhealth() <= 0f) {
            player2Castle.setImage(new Image(Objects.requireNonNull(getClass().getResource(playerBaseImagePaths[5])).toString()));
        }
    }

    private void spawnUnit(String unitType, Player player, double xPos, double yPos) {
        Unit unit = switch (unitType.toLowerCase()) {
            case "archer" -> new Archer(xPos, yPos);
            case "footman" -> new Footman(xPos, yPos);
            case "goblin" -> new Goblin(xPos, yPos);
            case "knight" -> new Knight(xPos, yPos);
            case "ogre" -> new Ogre(xPos, yPos);
            case "troll" -> new Troll(xPos, yPos);
            default -> null;
        };

        if (unit != null) {
            ImageView unitImage = new ImageView();
            String imagePath = "/icons/" + unitType + ".png";
            unitImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));

            unitImage.setFitWidth(unit.getW());
            unitImage.setFitHeight(unit.getH());
            unitImage.setLayoutX(unit.getX());
            unitImage.setLayoutY(unit.getY());

            unit.setImageView(unitImage);
            gamePane.getChildren().add(unitImage);

            if(player == player1) {
                player1Units.add(unit);
                moveUnit(player1, unit, unitImage, xPos, yPos, 1530, 655, player2Units);
            }else{
                player2Units.add(unit);
                moveUnit(player2, unit, unitImage, xPos, yPos, 350, 655, player1Units);
            }
        }
    }

    public void moveUnit(Player player, Unit unit, ImageView unitImage, double startX, double startY, double targetX, double targetY, List<Unit> opponentUnits) {
        int steps = unit.movementSpeed();
        double deltaX = (targetX - startX) / steps;
        double deltaY = (targetY - startY) / steps;

        Timeline moveUnit = new Timeline(
                new KeyFrame(Duration.millis(50), e -> {
                    unitImage.setLayoutX(unitImage.getLayoutX() + deltaX);
                    unitImage.setLayoutY(unitImage.getLayoutY() + deltaY);

                    double currentX = unitImage.getLayoutX();
                    double currentY = unitImage.getLayoutY();

//                    for (Unit opponentUnit : opponentUnits) {
//                        ImageView opponentImage = opponentUnit.getImageView();
//                        if (checkCollision(unitImage, opponentImage)) {
//                            handleUnitAttack(player, unit, opponentUnit, opponentImage);;
//                            return; // Exit movement if collision occurs
//                        }
//                    }

                    if (Math.abs(currentX - targetX) <= 1 && Math.abs(currentY - targetY) <= 1) {
                        handleCastleDamage(player, unit, unitImage);
                    }
                })
        );
        moveUnit.setCycleCount(steps);
        moveUnit.play();
    }

    public void handleCastleDamage(Player player, Unit unit, ImageView unitImage) {
        if(player == player1){
            player2.changeHealth(-unit.getBaseDamage());
            player2.validateHealth();
            System.out.println(player2.getName() + " took damage! Remaining Health: " + player2.getCurrhealth());
            ((Pane) unitImage.getParent()).getChildren().remove(unitImage);
            player1Units.remove(unit);
            player1.changeCurrCap(-unit.getUnitSize());
        }else{
            player1.changeHealth(-unit.getBaseDamage());
            player1.validateHealth();
            System.out.println(player1.getName() + " took damage! Remaining Health: " + player1.getCurrhealth());
            ((Pane) unitImage.getParent()).getChildren().remove(unitImage);
            player2Units.remove(unit);
            player2.changeCurrCap(-unit.getUnitSize());
        }
        updateUI();
    }

//
//    private boolean checkCollision(ImageView unit1, ImageView unit2) {
//        if (unit2 == null) {
//            System.out.println("unit2 is null!");
//            return false;
//        }
//
//        return unit1.getBoundsInParent().intersects(unit2.getBoundsInParent());
//    }
//
//    public void handleUnitAttack(Player player, Unit attacker, Unit defender, ImageView defenderImage) {
//        defender.setHealth(defender.getHealth() - attacker.getBaseDamage());
//
//        if (defender.getHealth() <= 0) {
//            ((Pane) defenderImage.getParent()).getChildren().remove(defenderImage);
//
//            if (player == player1) {
//                player2Units.remove(defender);
//                player2.changeCurrCap(-defender.getUnitSize());
//            } else {
//                player1Units.remove(defender);
//                player1.changeCurrCap(-defender.getUnitSize());
//            }
//
//            System.out.println("Unit defeated! Remaining health of defender: " + defender.getHealth());
//        } else {
//            System.out.println("Defender health: " + defender.getHealth());
//        }
//    }
//
//    private void removeUnit(Unit unit, ImageView unitImage, Player player) {
//        ((Pane) unitImage.getParent()).getChildren().remove(unitImage);
//        player.changeCurrCap(-unit.getUnitSize());
//
//        if (player == player1) {
//            player1Units.remove(unit);
//        } else {
//            player2Units.remove(unit);
//        }
//    }
}