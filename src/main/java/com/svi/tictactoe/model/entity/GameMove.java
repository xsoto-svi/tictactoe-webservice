package com.svi.tictactoe.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameMove {
  private UUID gameId;
  private UUID playerId;
  private String symbol;
  private int location;
  private LocalDateTime dateSave;

  public UUID getGameId() {
    return gameId;
  }

  public void setGameId(UUID gameId) {
    this.gameId = gameId;
  }

  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public int getLocation() {
    return location;
  }

  public void setLocation(int location) {
    this.location = location;
  }

  public LocalDateTime getDateSave() {
    return dateSave;
  }

  public void setDateSave(LocalDateTime dateSave) {
    this.dateSave = dateSave;
  }
}