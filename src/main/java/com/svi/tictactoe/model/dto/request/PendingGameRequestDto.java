package com.svi.tictactoe.model.dto.request;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotBlank;

public class PendingGameRequestDto {
  @NotBlank(message = "Player name cannot be blank")
  @JsonbProperty("playername")
  private String playerName;

  @NotBlank(message = "Room code cannot be blank")
  @JsonbProperty("roomcode")
  private String roomCode;

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public String getRoomCode() {
    return roomCode;
  }

  public void setRoomCode(String roomCode) {
    this.roomCode = roomCode;
  }
}
