package com.task.game.domain;

public record Move(String performedByPlayerId, int addedValue, long resultingNumber) { }
