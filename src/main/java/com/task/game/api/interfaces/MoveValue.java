package com.task.game.api.interfaces;

public enum MoveValue {
  PLUS_ONE(1), ZERO(0), MINUS_ONE(-1);


  private final int value;

  MoveValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
