package com.warofoop.warofoop;

import com.warofoop.warofoop.controllers.GameController;
import com.warofoop.warofoop.controllers.LobbyController;
import com.warofoop.warofoop.controllers.MenuController;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private final Stage stage;
    private final Scene mainMenuScene;
    private final Scene lobbyScene;
    private final Scene gameScene;

    public SceneManager(Stage stage) throws IOException {
        this.stage = stage;

        // Load main menu and set SceneManager for its controller
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Main_Menu.fxml"));
        Parent mainMenuRoot = mainMenuLoader.load();
        mainMenuScene = new Scene(mainMenuRoot);
        MenuController menuController = mainMenuLoader.getController();
        menuController.setSceneManager(this);

        // Load lobby and set SceneManager for its controller
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Lobby_Window.fxml"));
        Parent lobbyRoot = lobbyLoader.load();
        lobbyScene = new Scene(lobbyRoot);
        LobbyController lobbyController = lobbyLoader.getController();
        lobbyController.setSceneManager(this);

        // Load game and set SceneManager for its controller
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Game_Window.fxml"));
        Parent gameRoot = gameLoader.load();
        gameScene = new Scene(gameRoot);
        GameController gameController = gameLoader.getController();
        gameController.setSceneManager(this);
    }

    public void switchToMainMenu() {
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public void switchToLobby() {
        stage.setScene(lobbyScene);
        stage.show();
    }

    public void switchToGame() {
        stage.setScene(gameScene);
        stage.show();
    }
}
