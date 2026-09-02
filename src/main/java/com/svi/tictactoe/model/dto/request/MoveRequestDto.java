package com.svi.tictactoe.model.dto.request;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.UUID;

public class MoveRequestDto {

  @NotBlank(message = "Game ID cannot be blank")
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

  public String getGameId() {
    return gameId;
  }

  public String getPlayerName() {
    return playerName;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getLocation() {
    return location;
  }

  public LocalDateTime getDateSave() {
    return dateSave;
  }
}
