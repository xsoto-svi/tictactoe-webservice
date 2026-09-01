package com.svi.tictactoe.model.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

public class MoveRequestDto {

  @NotBlank(message = "Game ID cannot be blank")
  private String gameId;

  @NotBlank(message = "Player ID cannot be blank")
  private String playerId;

  @NotBlank(message = "Symbol cannot be blank")
  @Pattern(regexp = "[XO]", message = "Symbol must be either 'X' or 'O'")
  private String symbol;

  @Min(value = 0, message = "Location must be between 0 and 8")
  @Max(value = 8, message = "Location must be between 0 and 8")
  private int location;

  public String getGameId() {
    return gameId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public String getSymbol() {
    return symbol;
  }

  public int getLocation() {
    return location;
  }
}
