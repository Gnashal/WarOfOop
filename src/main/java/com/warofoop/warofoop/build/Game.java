package com.warofoop.warofoop.build;

enum GameState {
    END, ONGOING, DEFAULT
}

public class Game {
    private Player player1;
    private Player player2;
    private GameState gameState;
    private String map;
    private int roundCount;
    private int deploy_time; // Deployment phase time (20s)
    private int start_time;  // Combat phase time (30s)
    private int interval_time; // Interval between phases (2s)
    private long startTimeMillis; // Time when the game started

    public Game(Player player1, Player player2, String selectedMap) {
        this.player1 = player1;
        this.player2 = player2;
        this.gameState = GameState.DEFAULT;
        this.map = selectedMap;
        this.roundCount = 0;
        this.deploy_time = 20;
        this.start_time = 0;
        this.interval_time = 0;
    }

    public int getDeploy_time() {
        return deploy_time;
    }

    public int getStart_time() {
        return start_time;
    }

    public int getInterval_time() {
        return interval_time;
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
            startTimeMillis = System.currentTimeMillis(); // Start the time
            System.out.println("Starting game on map: " + map);
        }
    }

    public void endGame() {
        gameState = GameState.END;
        System.out.println("Game ended!");
    }

    public void deploy_time_tick() {
        if (deploy_time > 0) {
            deploy_time--;
        } else {
            interval_time = 2;
            deploy_time = 20;
        }
    }

    public void start_time_tick() {
        if (start_time > 0) {
            start_time--;
        } else {
            interval_time = 2;
            start_time = 30;
        }
    }

    public void interval_time_tick() {
        if (interval_time > 0) {
            interval_time--;
        } else {
            if (deploy_time == 20) {
                System.out.println("Starting combat phase...");
                start_time = 30;
            } else if (start_time == 30) {
                System.out.println("Starting deployment phase...");
                deploy_time = 20;
                roundCount++;
                System.out.println("Round " + roundCount + " begins!");
            }
        }
    }

    public void updateGameLogic() {
        if (gameState == GameState.ONGOING) {
            if (interval_time > 0) {
                interval_time_tick();
            } else if (deploy_time > 0) {
                deploy_time_tick();
            } else if (start_time > 0) {
                start_time_tick();
            }

            if (player1.getCurrhealth() <= 0 || player2.getCurrhealth() <= 0) {
                endGame();
            }
        }
    }

    public boolean isGameOver() {
        if(gameState == GameState.ONGOING && player1.getCurrhealth() <= 0 || player2.getCurrhealth() <= 0) {
            gameState = GameState.END;
            return true;
        }
        return false;
    }

    // Method to get the formatted time in seconds since the game started
    public String getFormattedTime() {
        long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
        long seconds = (elapsedMillis / 1000) % 60; // Get only seconds
        return String.valueOf(seconds);
    }
}
