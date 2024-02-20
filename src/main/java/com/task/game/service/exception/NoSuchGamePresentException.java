package com.task.game.service.exception;

public class NoSuchGamePresentException extends RuntimeException {
  public NoSuchGamePresentException(String gameId) {
    super(STR."No game found for id: \{gameId}");
  }
}
