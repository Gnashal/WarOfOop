package com.warofoop.warofoop.build;

 enum GameState {
    END, ONGOING, DEFAULT
}

public class Game {
    Player player1;
    Player player2;
    GameState gameState;
    int roundCount;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameState =GameState.DEFAULT;
        roundCount = 0;
    }
    public void startGame() {
        if (gameState == GameState.DEFAULT) {
            gameState = GameState.ONGOING;
            roundCount = 1;
            System.out.println("Game started!");
        }
    }

    public void nextRound() {
        if (gameState == GameState.ONGOING) {
            roundCount++;
        }
        if (player1.getCurrhealth() <= 0 || player2.getCurrhealth() <= 0) {
            endGame();
        }
    }

    public void endGame() {
        gameState = GameState.END;
        if (player1.getCurrhealth() <= 0) {
            System.out.println("PLAYER 2 WINS");
        } else if (player2.getCurrhealth() <= 0) {
            System.out.println("PLAYER 1 WINS");
        }
    }




}
