package com.warofoop.warofoop.build;

 enum GameState {
    END, ONGOING, DEFAULT
}

public class Lobby {
    Player player1;
    Player player2;
    GameState gameState;
    int roundCount;
}
