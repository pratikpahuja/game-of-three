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

/**
 * Listens to notify player event.
 * - In case the player to be notified of their turn has input type Manual, a notification is printed to console.
 * - In case the player to be notified of their turn has input type Automatic
 *  - Next move value is calculated(currently logic lies in this class but ideally should be separated)
 *  - And, move is made by calling GameService::makeMove() method.
 */
@RequiredArgsConstructor
@Component
public class NotifyPlayerTurnEventListener {
  private final GameService gameService;
  private final GameRepository gameRepository;

  @JmsListener(destination = NOTIFY_PLAYER_TURN_QUEUE_NAME)
  public void notifyPlayer(NotifyPlayerTurnEvent event) {
    var game = getGameOrFail(event.gameId());
    var player = getPlayerOrFail(game, event.playerId());

    if (player.getInputType() == InputType.AUTOMATIC) {
      var calculatedMoveInput = calculateMoveInput(game.getCurrentNumber());
      gameService.makeMove(game.getId(), player.getId(), MakeMoveSourceType.AUTOMATIC, calculatedMoveInput);
    } else
      System.err.println(STR."Notification for player: \{player.getId()}, It's your turn, current number: \{game.getCurrentNumber()}.");
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

  private int calculateMoveInput(long currentNumber) {
    var result = currentNumber % 3;

    if (result == 2)
      return 1;
    if (result == 1)
      return -1;

    return 0;
  }
}
