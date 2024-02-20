package com.task.game.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
  public BadRequestException(RuntimeException e) {
    super(HttpStatus.BAD_REQUEST, e.getMessage(), e);
  }
}
