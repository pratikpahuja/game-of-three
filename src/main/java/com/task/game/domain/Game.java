package com.task.game.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Game {
  @Setter
  private String id;
  private long initNumber;
  private long currentNumber;
  //ArrayBlockingQueue is a bounded collection and also provides thread safety, so used for this task.
  private ArrayBlockingQueue<Player> players;
  private List<Move> moves;
  private boolean inProgress;

  @JsonIgnore
  @EqualsAndHashCode.Exclude
  private Map<String, Player> playersMap = new ConcurrentHashMap<>();
  @JsonIgnore
  @EqualsAndHashCode.Exclude
  private Map<Integer, Player> playersSequenceMap = new ConcurrentHashMap<>();
  private boolean finished;

  public Game(Player initPlayer, long initNumber, int maxPlayers) {
    this.initNumber = initNumber;
    this.currentNumber = initNumber;
    players = new ArrayBlockingQueue<>(maxPlayers, false);
    players.add(initPlayer);
    moves = new ArrayList<>();
  }

  public int getPlayersCount() {
    return players.size();
  }

  public void addPlayer(Player savedPlayer) {
    this.players.add(savedPlayer);
  }

  public void addMove(Move move) {
    this.moves.add(move);
  }

  public void start() {
    validateIfAlreadyInProgress();

    var sequence = 0;
    for (var player: players) {
      playersMap.put(player.getId(), player);
      playersSequenceMap.put(sequence++, player);
    }

    inProgress = true;
  }

  public void markFinish() {
    this.finished = true;
    this.inProgress = false;
  }

  public boolean hasFinished() {
    return this.finished;
  }

  public Player nextPlayer() {
    return playersSequenceMap.get((moves.size() + 1) % getPlayersCount());
  }

  public Optional<Player> getPlayerDetails(String playerId) {
    return Optional.ofNullable(playersMap.get(playerId));
  }

  private void validateIfAlreadyInProgress() {
    if (inProgress)
      throw new IllegalStateException("Game is already in progress.");
  }
}
