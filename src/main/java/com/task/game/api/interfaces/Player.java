package com.task.game.api.interfaces;

import com.task.game.domain.InputType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Player {
  @NotNull
  private InputType inputType;
}
