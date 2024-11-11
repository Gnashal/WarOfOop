import com.warofoop.warofoop.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class LobbyController
{
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

    public void p1p2_ready() throws IOException {
        if (player1Ready && player2Ready) {
            statusLabel.setText("Both players are ready! Starting the game...");
            startGame();
        } else {
            updateStatusLabel();
        }
    }

    public void toggle_player1(ActionEvent event) throws IOException {
        if (!player1Ready) {
            player1Ready = true;
            player1Button.setText("Unready");
        } else {
            player1Ready = false;
            player1Button.setText("Ready!");
        }
        p1p2_ready();
    }

    public void toggle_player2(ActionEvent event) throws IOException {
        if (!player2Ready) {
            player2Ready = true;
            player2Button.setText("Unready");
        } else {
            player2Ready = false;
            player2Button.setText("Ready!");
        }
        p1p2_ready();
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
    
    @FXML
    public void goToGame() throws IOException {
        System.out.println("Entering Game!");
        new SceneManager(lobbyPane, "Game_Window.fxml");
    }
}
