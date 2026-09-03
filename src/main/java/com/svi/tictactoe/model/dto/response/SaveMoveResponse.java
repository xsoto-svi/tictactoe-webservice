package com.svi.tictactoe.model.dto.response;

public class SaveMoveResponse {
  private GameMoveDto gameMoveDto;
  private String message;

  public SaveMoveResponse(GameMoveDto gameMoveDto, String message) {
    this.gameMoveDto = gameMoveDto;
    this.message = message;
  }

  public GameMoveDto getGameMoveDto() {
    return gameMoveDto;
  }

  public void setGameMoveDto(GameMoveDto gameMoveDto) {
    this.gameMoveDto = gameMoveDto;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
