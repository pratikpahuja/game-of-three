package com.task.game.api.interfaces;

import com.task.game.domain.Game;
import com.task.game.domain.Player;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record JoinGameResponse(Game activeGame, Player joinedPlayer) { }
