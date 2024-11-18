package com.warofoop.warofoop;

import com.warofoop.warofoop.build.*;
import com.warofoop.warofoop.controllers.GameController;
import com.warofoop.warofoop.controllers.LobbyController;
import com.warofoop.warofoop.controllers.MenuController;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private final Stage stage;
    private final Scene mainMenuScene;
    private final Scene loadingScene;
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

        FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/loading_screen.fxml"));
        Parent loadingRoot = loadingLoader.load();
        loadingScene = new Scene(loadingRoot);

    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    private void enforceFullScreen() {
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
        }
    }

    public void switchToMainMenu() {
        showLoading(() -> {
            stage.setScene(mainMenuScene);
            enforceFullScreen();
        });

    }

    public void switchToLobby() {
        showLoading(() -> {
            stage.setScene(lobbyScene);
            enforceFullScreen();
        });
    }

    public void switchToGame(Game game) throws IOException {
        showLoading(() -> {
            try {
                FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Game_Window.fxml"));
                Parent gameRoot = gameLoader.load();
                gameScene = new Scene(gameRoot);
                gameController = gameLoader.getController();
                gameController.setSceneManager(this);
                gameController.setGame(game);

                stage.setScene(gameScene);
                enforceFullScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }

    public void reloadLobby() throws IOException {
        showLoading(() -> {
            try {
                FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/com/warofoop/warofoop/Lobby_Window.fxml"));
                Parent lobbyRoot = lobbyLoader.load();
                LobbyController lobbyController = lobbyLoader.getController();
                lobbyController.setSceneManager(this);
                lobbyScene = new Scene(lobbyRoot);
                stage.setScene(lobbyScene);
                enforceFullScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void showLoading(Runnable sceneSetup) {
        stage.setScene(loadingScene);
        enforceFullScreen();

        // Run the scene setup task on a background thread
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate loading time (adjust as needed)
                Platform.runLater(sceneSetup);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
