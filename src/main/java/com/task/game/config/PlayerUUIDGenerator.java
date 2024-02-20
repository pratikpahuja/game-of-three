package com.task.game.config;

import org.springframework.stereotype.Component;

import static java.util.UUID.randomUUID;

@Component
public class PlayerUUIDGenerator implements UUIDGenerator {

  @Override
  public String uuid() {
    return STR."P-\{randomUUID().toString()}";
  }

}
