package com.task.game.service.exception;

public class InvalidPlayerException extends RuntimeException {
  public InvalidPlayerException(String gameId, String playerId) {
    super(STR."Player id: \{playerId} is not a participant of game: \{gameId}");
  }
}
