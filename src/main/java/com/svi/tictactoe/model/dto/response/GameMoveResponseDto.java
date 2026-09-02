package com.svi.tictactoe.model.dto.response;

import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class GameMoveResponseDto {
  @JsonbProperty("gameid")
  private UUID gameId;

  @JsonbProperty("playerid")
  private String playerName;

  @JsonbProperty("symbol")
  private String symbol;

  @JsonbProperty("location")
  private int location;

  @JsonbProperty("datesaved")
  private LocalDateTime dateSaved;

  public UUID getGameId() {
    return gameId;
  }

  public void setGameId(UUID gameId) {
    this.gameId = gameId;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
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

  public LocalDateTime getDateSaved() {
    return dateSaved;
  }

  public void setDateSaved(LocalDateTime dateSaved) {
    this.dateSaved = dateSaved;
  }
}
