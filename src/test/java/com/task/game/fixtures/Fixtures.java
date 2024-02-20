package com.task.game.fixtures;

import com.task.game.domain.InputType;
import com.task.game.service.Game;
import com.task.game.service.Player;

public class Fixtures {

  public static Game sampleGame() {
    return new Game(57);
  }

  public static Player samplePlayer() {
    return new Player(InputType.MANUAL);
  }

  public static com.task.game.domain.Player samplePlayerEntity() {
    return new com.task.game.domain.Player(InputType.MANUAL);
  }

  public static com.task.game.domain.Player samplePlayerEntityWithId() {
    var player = samplePlayerEntity();
    player.setId("player-uuid");
    return player;
  }

  public static com.task.game.domain.Game sampleGameEntity() {
    var player = samplePlayerEntityWithId();
    return new com.task.game.domain.Game(player, 57, 2);
  }

  public static com.task.game.domain.Game sampleGameEntityWithId() {
    var game = sampleGameEntity();
    game.setId("game-uuid");

    return game;
  }
}
