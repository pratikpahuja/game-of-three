package com.task.game.service;

import com.task.game.domain.Game;
import com.task.game.domain.Player;

public record JoinGameOutput(Game game, Player joinedPlayer) { }
