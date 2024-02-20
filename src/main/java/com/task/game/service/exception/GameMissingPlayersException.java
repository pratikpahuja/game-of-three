package com.task.game.service.exception;

public class GameMissingPlayersException extends RuntimeException {
  public GameMissingPlayersException(String gameId) {
    super(STR."Game: \{gameId} does not have enough players yet to start the game.");
  }
}
