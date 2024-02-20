package com.task.game.api.interfaces;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import static java.lang.Long.MAX_VALUE;

public record CreateGameRequest(
  @Min(value = 2, message = "startNumber: cannot be less than 2.")
  @Max(value = MAX_VALUE, message = "startNumber: cannot be more than Long.MAX_VALUE")
  long initNumber,
  @Valid @NotNull Player player) { }
