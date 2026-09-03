package com.svi.tictactoe.model.dto.request;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class MoveRequestDto {

  @NotNull(message = "Game ID cannot be blank")
  @Pattern(
          regexp = "^[A-Z0-9]{4}R*_[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
          message = "Invalid UUID format for gameId."
  )
  @JsonbProperty("gameid")
  private String gameId;

  @NotBlank(message = "Player ID cannot be blank")
  @JsonbProperty("playername")
  private String playerName;

  @NotBlank(message = "Symbol cannot be blank")
  @Pattern(regexp = "[XO]", message = "Symbol must be either 'X' or 'O'")
  private String symbol;

  @Min(value = 0, message = "Location must be between 0 and 8")
  @Max(value = 8, message = "Location must be between 0 and 8")
  private int location;

  @JsonbProperty("datetime")
  private LocalDateTime dateSave;

  public MoveRequestDto() {
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
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

  public LocalDateTime getDateSave() {
    return dateSave;
  }

  public void setDateSave(LocalDateTime dateSave) {
    this.dateSave = dateSave;
  }
}
