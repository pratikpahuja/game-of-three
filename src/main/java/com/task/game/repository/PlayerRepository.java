package com.task.game.repository;

import com.task.game.config.UUIDGenerator;
import com.task.game.domain.Game;
import com.task.game.domain.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerRepository {
  private final UUIDGenerator playerUUIDGenerator;

  public Player save(Player player) {
    player.setId(playerUUIDGenerator.uuid());

    return player;
  }

}
