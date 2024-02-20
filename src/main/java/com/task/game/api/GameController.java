package com.task.game.api;

import com.task.game.api.exception.BadRequestException;
import com.task.game.api.exception.NoActiveGameException;
import com.task.game.api.exception.ResourceNotFoundException;
import com.task.game.api.interfaces.CreateGameRequest;
import com.task.game.api.interfaces.JoinGameRequest;
import com.task.game.api.interfaces.JoinGameResponse;
import com.task.game.api.interfaces.MakeMoveRequest;
import com.task.game.service.Game;
import com.task.game.service.GameService;
import com.task.game.service.MakeMoveSourceType;
import com.task.game.service.Player;
import com.task.game.service.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

  private final GameService gameService;

  @PostMapping
  com.task.game.domain.Game createGame(@RequestBody CreateGameRequest request) {
    return gameService.createGame(toGame(request), toPlayer(request));
  }

  @GetMapping
  com.task.game.domain.Game getActiveGame() {
    return gameService.getActiveGame()
      .orElseThrow(NoActiveGameException::new);
  }

  @PostMapping("{gameId}/join")
  JoinGameResponse joinGame(@PathVariable("gameId") String gameId, @RequestBody JoinGameRequest request) {
    try {
      var output = gameService.joinGame(gameId, toPlayer(request));
      return new JoinGameResponse(output.game(), output.joinedPlayer());
    } catch (NoSuchGamePresentException e) {
      throw new ResourceNotFoundException(e);
    } catch (MaxPlayersJoinedException e) {
      throw new BadRequestException(e);
    }
  }

  @PostMapping("{gameId}/{playerId}/move")
  com.task.game.domain.Game makeMove(@PathVariable("gameId") String gameId,
                @PathVariable("playerId") String playerId,
                @RequestBody MakeMoveRequest request) {
    try {
      return gameService.makeMove(gameId, playerId, MakeMoveSourceType.MANUAL, request.value().getValue());
    } catch (GameAlreadyFinishedException | GameMissingPlayersException
             | IllegalMoveException | NotYourTurnException
             | WrongInputSelectionException e) {
      throw new BadRequestException(e);
    } catch (NoSuchGamePresentException | InvalidPlayerException e) {
      throw new ResourceNotFoundException(e);
    }
  }

  private Game toGame(CreateGameRequest request) {
    return new Game(request.initNumber());
  }

  private Player toPlayer(CreateGameRequest request) {
    return new Player(request.player().getInputType());
  }

  private Player toPlayer(JoinGameRequest request) {
    return new Player(request.player().getInputType());
  }

}
