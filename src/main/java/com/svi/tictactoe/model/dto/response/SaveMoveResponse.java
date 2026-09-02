package com.svi.tictactoe.model.dto.response;

import java.util.List;
import java.util.UUID;

public class SaveMoveResponse {
  private GameMoveResponseDto gameMoveDto;
  private String message;

  public SaveMoveResponse(GameMoveResponseDto gameMoveDto, String message) {
    this.gameMoveDto = gameMoveDto;
    this.message = message;
  }

  public GameMoveResponseDto getGameMoveDto() {
    return gameMoveDto;
  }

  public void setGameMoveDto(GameMoveResponseDto gameMoveDto) {
    this.gameMoveDto = gameMoveDto;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
