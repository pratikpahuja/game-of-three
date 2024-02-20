package com.task.game.service.exception;

public class NotYourTurnException extends RuntimeException {
  public NotYourTurnException(String gameId, String playerId) {
    super(STR."It is not not your turn, game: \{gameId}, player: \{playerId}");
  }
}
