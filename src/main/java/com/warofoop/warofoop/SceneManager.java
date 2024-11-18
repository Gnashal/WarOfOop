package com.warofoop.warofoop;

import com.warofoop.warofoop.build.*;
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
    private  Scene lobbyScene;
    private  Scene gameScene;


    private  GameController gameController;

    public SceneManager(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Main_Menu.fxml"));
        Parent mainMenuRoot = mainMenuLoader.load();
        mainMenuScene = new Scene(mainMenuRoot);
        MenuController menuController = mainMenuLoader.getController();
        menuController.setSceneManager(this);

        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Lobby_Window.fxml"));
        Parent lobbyRoot = lobbyLoader.load();
        lobbyScene = new Scene(lobbyRoot);
        LobbyController lobbyController = lobbyLoader.getController();
        lobbyController.setSceneManager(this);


    }

    public void switchToMainMenu() {
        stage.setScene(mainMenuScene);
        stage.show();
    }

    public void switchToLobby() {
        stage.setScene(lobbyScene);
        stage.show();
    }

    public void switchToGame(Game game) throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Game_Window.fxml"));
        Parent gameRoot = gameLoader.load();
        gameScene = new Scene(gameRoot);
        gameController = gameLoader.getController();
        gameController.setSceneManager(this);
        gameController.setGame(game);
        stage.setScene(gameScene);
        stage.show();
        stage.setFullScreen(true);
    }

    public void reloadLobby() throws IOException {
        FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Lobby_Window.fxml"));
        Parent lobbyRoot = lobbyLoader.load();
        LobbyController lobbyController = lobbyLoader.getController();
        lobbyController.setSceneManager(this);
        lobbyScene = new Scene(lobbyRoot);
        stage.setScene(lobbyScene);
        stage.show();
    }

}
