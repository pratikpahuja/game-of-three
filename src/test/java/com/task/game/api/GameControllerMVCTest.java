package com.task.game.api;

import com.task.game.service.GameService;
import com.task.game.service.exception.NoSuchGamePresentException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.task.game.fixtures.Fixtures.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
class GameControllerMVCTest {
  @MockBean GameService gameService;
  @Autowired MockMvc mvc;


  @Test
  void getActiveGameWhenNoneExists() throws Exception {
    performGetActiveGame()
      .andExpect(status().isNotFound());
  }

  @Test
  void joinGameWhenNotExists() throws Exception {
    when(gameService.joinGame("game-uuid", samplePlayer())).thenThrow(NoSuchGamePresentException.class);
    performJoinGame("game-uuid")
      .andExpect(status().isNotFound());

    verify(gameService).joinGame("game-uuid", samplePlayer());
  }

  private ResultActions performJoinGame(String gameId) throws Exception {
    var requestBody = """
      {
          "player": {
              "inputType": "MANUAL"
          }
      }
      """;

    return mvc.perform(post(STR."/api/v1/games/\{gameId}/join")
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestBody));
  }

  ResultActions performGetActiveGame() throws Exception {
    return mvc.perform(get("/api/v1/games"));
  }
}