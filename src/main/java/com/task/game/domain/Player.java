package com.task.game.domain;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class Player {

  @Setter
  private String id;
  private InputType inputType;

  public Player(InputType inputType) {
    this.inputType = inputType;
  }

}
