package com.task.game.api.interfaces;

import jakarta.validation.constraints.NotNull;

public record MakeMoveRequest (@NotNull MoveValue value) { }
