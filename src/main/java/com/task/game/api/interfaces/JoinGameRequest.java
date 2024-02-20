package com.task.game.api.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static java.lang.Long.MAX_VALUE;

public record JoinGameRequest(
  @Valid @NotNull Player player) { }
