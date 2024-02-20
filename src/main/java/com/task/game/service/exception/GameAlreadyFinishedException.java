package com.task.game.service.exception;

public class GameAlreadyFinishedException extends RuntimeException {
  public GameAlreadyFinishedException(String gameId) {
    super(STR."Game: \{gameId} has already finished.");
  }
}
