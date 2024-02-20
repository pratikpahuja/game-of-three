package com.task.game.service.exception;

import com.task.game.domain.InputType;
import com.task.game.service.MakeMoveSourceType;

public class IllegalMoveException extends RuntimeException {
  public IllegalMoveException(String playerId, InputType playerInputType, MakeMoveSourceType moveType) {
    super(STR."Illegal move with type: \{moveType.name()} performed for playerId: \{playerId} with input type: \{playerInputType.name()}");
  }
}
