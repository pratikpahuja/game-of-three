package com.task.game.service.exception;

public class WrongInputSelectionException extends RuntimeException {
  public WrongInputSelectionException(String gameId, String playerId, int moveValue) {
    super("Supplied value is not does not fulfill the expected requirements(added sum divisible by 3)"
      + STR.", gameId: \{gameId}, playerId: \{playerId}, \{moveValue}");
  }
}
