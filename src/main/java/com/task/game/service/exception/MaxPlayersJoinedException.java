package com.task.game.service.exception;

public class MaxPlayersJoinedException extends RuntimeException {
  public MaxPlayersJoinedException(String gameId, int maxPlayersAllowed) {
    super(STR."Max players reached for game: \{gameId}, max players allowed: \{maxPlayersAllowed}");
  }
}
