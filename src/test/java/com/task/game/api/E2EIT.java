package com.task.game.api;

import com.task.game.api.interfaces.MoveValue;
import com.task.game.domain.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class E2EIT {

  @Autowired TestRestTemplate restTemplate;

  //Starting with number 10.
  @Test
  void testExampleGame() {
    var createdGame = performCreateGame();
    var activeGame = performGetActiveGame();

    assertThat(createdGame.getId(), is(activeGame.getId()));
    assertThat(activeGame.getPlayersCount(), is(1));

    performJoinGame(createdGame.getId());
    activeGame = performGetActiveGame();
    assertThat(activeGame.getPlayersCount(), is(2));
    assertThat(activeGame.getMoves(), hasSize(0));

    var player1Id = activeGame.getPlayers().poll().getId();
    var player2Id = activeGame.getPlayers().poll().getId();

    //Starting value: 10
    performMove(createdGame.getId(), player2Id, MoveValue.MINUS_ONE);
    activeGame = performGetActiveGame();
    assertThat(activeGame.getCurrentNumber(), is(3L));
    assertThat(activeGame.getMoves(), hasSize(1));
    //Move: -1, value: 3

    //Starting value: 3
    performMove(createdGame.getId(), player1Id, MoveValue.ZERO);
    activeGame = performGetActiveGame();
    assertThat(activeGame.getMoves(), hasSize(2));
    assertThat(activeGame.hasFinished(), is(true));
    //Move: 0, Win

  }

  Game performCreateGame() {
    var body = """
      {
          "initNumber": 10,
          "player": {
              "inputType": "MANUAL"
          }
      }
      """;
    var request = RequestEntity.post("/api/v1/games")
      .contentType(MediaType.APPLICATION_JSON)
      .body(body);

    return restTemplate.exchange(request, Game.class).getBody();
  }

  void performJoinGame(String gameUuid) {
    var body = """
      {
          "player": {
              "inputType": "MANUAL"
          }
      }
      """;
    var request = RequestEntity.post(STR."/api/v1/games/\{gameUuid}/join")
      .contentType(MediaType.APPLICATION_JSON)
      .body(body);

    restTemplate.exchange(request, Game.class).getBody();
  }

  void performMove(String gameUuid, String playerUuid, MoveValue moveValue) {
    var body = STR."""
      {
          "value": "\{moveValue.name()}"
      }
      """;
    var request = RequestEntity.post(STR."/api/v1/games/\{gameUuid}/\{playerUuid}/move")
      .contentType(MediaType.APPLICATION_JSON)
      .body(body);

    restTemplate.exchange(request, Game.class).getBody();
  }

  Game performGetActiveGame() {
    var request = RequestEntity.get("/api/v1/games")
      .build();

    return restTemplate.exchange(request, Game.class).getBody();
  }

}
