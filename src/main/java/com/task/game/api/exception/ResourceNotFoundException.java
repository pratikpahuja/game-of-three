package com.task.game.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {

  public ResourceNotFoundException(Exception e) {
    super(HttpStatus.NOT_FOUND, e.getMessage(), e);
  }

}
