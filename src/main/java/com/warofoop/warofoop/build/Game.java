package com.warofoop.warofoop.build;

 enum GameState {
    END, ONGOING, DEFAULT
}

public class Game {
    Player player1;
    Player player2;
    GameState gameState;
    String map;
    int roundCount;

    public Game(Player player1, Player player2, String selectedMap) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameState =GameState.DEFAULT;
        this.map = selectedMap;
        roundCount = 0;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getRoundCount() {
        return roundCount;
    }

    public String getMap() {
        return map;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void startGame() {
        if (gameState == GameState.DEFAULT) {
            gameState = GameState.ONGOING;
            roundCount = 1;
            System.out.println("Starting game with map: " + map);
            System.out.println("Game started!");
        }
    }

    public void endGame() {
        this.gameState = GameState.END;
    }

   public void updateGameLogic() {
        if (player1.getCurrhealth() <= 0 || player2.getCurrhealth() <= 0 ) {
            endGame();
        }

//        TODO: ADD UPDATES TO GAME LOGIC HERE
        roundCount++;
   }

  public boolean isGameOver() {
        return gameState == GameState.END;
  }




}
