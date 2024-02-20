package com.task.game.service;

import com.task.game.domain.InputType;
import com.task.game.repository.GameRepository;
import com.task.game.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jms.core.JmsTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import static com.task.game.fixtures.Fixtures.*;

class GameServiceTest {

  GameService gameService;
  private GameRepository gameRespository;
  private PlayerRepository playerRepository;

  @BeforeEach
  void setup() {
    gameRespository = mock(GameRepository.class);
    playerRepository = mock(PlayerRepository.class);
    var jmsTemplate = mock(JmsTemplate.class);
    gameService = new GameService(gameRespository, playerRepository, jmsTemplate);
    gameService.maxPlayersAllowed = 2;
  }

  @Test
  void testCreate() {
    com.task.game.domain.Player savedPlayer = samplePlayerEntityWithId();
    when(playerRepository.save(samplePlayerEntity())).thenReturn(savedPlayer);
    when(gameRespository.save(sampleGameEntity())).thenReturn(sampleGameEntityWithId());

    var sampleGame = sampleGame();
    var samplePlayer = samplePlayer();
    var game = gameService.createGame(sampleGame, samplePlayer);

    verify(playerRepository).save(samplePlayerEntity());
    verify(gameRespository).save(sampleGameEntity());
    assertThat(game.getCurrentNumber(), is(57L));
    assertThat(game.getPlayers().peek(), is(savedPlayer));
  }

}