package com.task.game.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoActiveGameException extends ResponseStatusException {
  public NoActiveGameException() {
    super(HttpStatus.NOT_FOUND, "No active game running at the moment.");
  }
}
