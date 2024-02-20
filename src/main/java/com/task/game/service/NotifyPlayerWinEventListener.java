package com.task.game.service;

import com.task.game.domain.Game;
import com.task.game.domain.InputType;
import com.task.game.domain.Player;
import com.task.game.repository.GameRepository;
import com.task.game.service.exception.InvalidPlayerException;
import com.task.game.service.exception.NoSuchGamePresentException;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.task.game.service.MessageQueueConstants.NOTIFY_PLAYER_TURN_QUEUE_NAME;
import static com.task.game.service.MessageQueueConstants.NOTIFY_PLAYER_WIN_QUEUE_NAME;

@RequiredArgsConstructor
@Component
public class NotifyPlayerWinEventListener {
  private final GameRepository gameRepository;

  @JmsListener(destination = NOTIFY_PLAYER_WIN_QUEUE_NAME)
  public void notifyPlayer(NotifyPlayerWinEvent event) {
    var game = getGameOrFail(event.gameId());
    var player = getPlayerOrFail(game, event.playerId());

    System.err.println(STR."Player: \{player.getId()} has won \{game.getId()}.");
  }

  private Game getGameOrFail(String gameId) {
    return gameRepository.getActiveGame()
      .filter(g -> g.getId().equals(gameId))
      .orElseThrow(() -> new NoSuchGamePresentException(gameId));
  }

  private Player getPlayerOrFail(Game game, String playerId) {
    return game.getPlayerDetails(playerId)
      .orElseThrow(() -> new InvalidPlayerException(game.getId(), playerId));
  }
}
