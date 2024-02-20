package com.task.game.repository;

import com.task.game.config.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.task.game.fixtures.Fixtures.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerRepositoryTest {

  PlayerRepository repository;
  private UUIDGenerator uuidGenerator;

  @BeforeEach
  void setup() {
    uuidGenerator = mock(UUIDGenerator.class);
    repository = new PlayerRepository(uuidGenerator);
  }

  @Test
  void testSave() {
    when(uuidGenerator.uuid()).thenReturn("player-uuid");
    var playerEntity = repository.save(samplePlayerEntity());

    assertThat(playerEntity, is(samplePlayerEntityWithId()));
  }
}