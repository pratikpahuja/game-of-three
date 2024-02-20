package com.task.game.service;

import com.task.game.domain.InputType;
import com.task.game.domain.Move;
import com.task.game.repository.GameRepository;
import com.task.game.repository.PlayerRepository;
import com.task.game.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.task.game.service.MessageQueueConstants.NOTIFY_PLAYER_TURN_QUEUE_NAME;
import static com.task.game.service.MessageQueueConstants.NOTIFY_PLAYER_WIN_QUEUE_NAME;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameRepository gameRepository;
  private final PlayerRepository playerRepository;
  private final JmsTemplate jmsTemplate;
  @Value("${game.max-players-allowed}")
  int maxPlayersAllowed;

  public com.task.game.domain.Game createGame(Game game, Player player) {
    var savedPlayer = savePlayer(player);
    return saveGame(game, savedPlayer);
  }

  public Optional<com.task.game.domain.Game> getActiveGame() {
    return gameRepository.getActiveGame();
  }

  public JoinGameOutput joinGame(String gameId, Player player) {
    var activeGame = getGameOrFail(gameId);

    validateMaxPlayersJoined(gameId, activeGame);

    var savedPlayer = savePlayer(player);
    addPlayerToGame(gameId, activeGame, savedPlayer);

    if (activeGame.getPlayersCount() == maxPlayersAllowed) {
      activeGame.start();
      var nextPlayer = activeGame.nextPlayer();
      notifyNextPlayer(gameId, nextPlayer);
    }

    //Sending combination of game and player as in case players allowed are > 2.
    //The determination of joined player from a particular thread will not be possible for client.
    return new JoinGameOutput(activeGame, savedPlayer);
  }

  public com.task.game.domain.Game makeMove(String gameId, String playerId, MakeMoveSourceType moveType, int moveValue) {
    var game = getGameOrFail(gameId);
    var player = getPlayerOrFail(game, playerId);

    validateGameState(game);
    validateRequiredPlayersJoinedGame(gameId, game);
    validateMoveSourceTypeWithPlayerInputType(moveType, player);
    validateInput(gameId, playerId, moveValue, game);
    validateCorrectTurn(gameId, playerId, game);

    performMove(gameId, playerId, moveValue, game, player);
    return game;
  }

  private void performMove(String gameId, String playerId, int moveValue, com.task.game.domain.Game game, com.task.game.domain.Player player) {
    //Synchronized to avoid multiple threads trying to make changes to same game
    synchronized (game) {
      long nextNumber = (game.getCurrentNumber() + moveValue) / 3;
      game.addMove(new Move(playerId, moveValue, nextNumber));

      //Winning condition
      if(nextNumber == 1) {
        game.markFinish();
        notifyWinningPlayer(gameId, player);
        return;
      }

      game.setCurrentNumber(nextNumber);
      var nextPlayer = game.nextPlayer();
      notifyNextPlayer(gameId, nextPlayer);
    }
  }

  private void validateGameState(com.task.game.domain.Game game) {
    if (game.hasFinished())
      throw new GameAlreadyFinishedException(game.getId());
  }

  private void validateCorrectTurn(String gameId, String playerId, com.task.game.domain.Game game) {
    if (!game.nextPlayer().getId().equals(playerId)) {
      throw new NotYourTurnException(gameId, playerId);
    }
  }

  private void validateInput(String gameId, String playerId, int moveValue, com.task.game.domain.Game game) {
    if ((game.getCurrentNumber() + moveValue) % 3 != 0) {
      throw new WrongInputSelectionException(gameId, playerId, moveValue);
    }
  }

  private void validateMoveSourceTypeWithPlayerInputType(MakeMoveSourceType moveType, com.task.game.domain.Player player) {
    if ((moveType == MakeMoveSourceType.AUTOMATIC && player.getInputType() == InputType.AUTOMATIC)
      || (moveType == MakeMoveSourceType.MANUAL && player.getInputType() == InputType.MANUAL))
      return;

    throw new IllegalMoveException(player.getId(), player.getInputType(), moveType);
  }

  private com.task.game.domain.Player getPlayerOrFail(com.task.game.domain.Game game, String playerId) {
    return game.getPlayerDetails(playerId)
      .orElseThrow(() -> new InvalidPlayerException(game.getId(), playerId));
  }

  private void validateRequiredPlayersJoinedGame(String gameId, com.task.game.domain.Game game) {
    if (game.getPlayersCount() != maxPlayersAllowed)
      throw new GameMissingPlayersException(gameId);
  }

  private void validateMaxPlayersJoined(String gameId, com.task.game.domain.Game activeGame) {
    if (activeGame.getPlayersCount() >= maxPlayersAllowed)
      throw new MaxPlayersJoinedException(gameId, maxPlayersAllowed);
  }

  private void addPlayerToGame(String gameId, com.task.game.domain.Game activeGame, com.task.game.domain.Player savedPlayer) {
    try {
      activeGame.addPlayer(savedPlayer);
    } catch (IllegalStateException e) {
      //In case multiple threads try to enroll player into game, ArrayBlockingQueue will throw IllegalStateException
      throw new MaxPlayersJoinedException(gameId, maxPlayersAllowed);
    }
  }

  private com.task.game.domain.Game getGameOrFail(String gameId) {
    return gameRepository.getActiveGame()
      .filter(g -> g.getId().equals(gameId))
      .orElseThrow(() -> new NoSuchGamePresentException(gameId));
  }

  private com.task.game.domain.Player savePlayer(Player player) {
    return playerRepository.save(new com.task.game.domain.Player(player.inputType()));
  }

  private com.task.game.domain.Game saveGame(Game game, com.task.game.domain.Player initPlayer) {
    return gameRepository.save(new com.task.game.domain.Game(initPlayer, game.initNumber(), maxPlayersAllowed));
  }

  private void notifyNextPlayer(String gameId, com.task.game.domain.Player nextPlayer) {
    jmsTemplate.convertAndSend(NOTIFY_PLAYER_TURN_QUEUE_NAME, new NotifyPlayerTurnEvent(gameId, nextPlayer.getId()));
  }

  private void notifyWinningPlayer(String gameId, com.task.game.domain.Player winningPlayer) {
    jmsTemplate.convertAndSend(NOTIFY_PLAYER_WIN_QUEUE_NAME, new NotifyPlayerWinEvent(gameId, winningPlayer.getId()));
  }
}
