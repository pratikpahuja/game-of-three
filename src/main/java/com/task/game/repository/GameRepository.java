package com.task.game.repository;

import com.task.game.config.UUIDGenerator;
import com.task.game.domain.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Optional.*;

@Service
@RequiredArgsConstructor
public class GameRepository {
  private final UUIDGenerator gameUUIDGenerator;
  private Optional<Game> activeGame = empty();

  public Game save(Game game) {
    game.setId(gameUUIDGenerator.uuid());

    activeGame = of(game);
    return game;
  }

  public Optional<Game> getActiveGame() {
    return activeGame;
  }
}
