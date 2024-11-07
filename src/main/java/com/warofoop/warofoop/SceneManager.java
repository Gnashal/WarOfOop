package com.warofoop.warofoop;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {
    public SceneManager(AnchorPane curr_ap, String fxml) throws IOException {
        AnchorPane next_ap = FXMLLoader.load(Objects.requireNonNull(App.class.getResource(fxml)));
        curr_ap.getChildren().clear();
        curr_ap.getChildren().setAll(next_ap);
    }


}
