package com.svi.tictactoe.model.dto.response;

import java.util.List;

public class GameDetailsResponse {
  private final List<GameMoveResponseDto> list;

  private final String message;

  public GameDetailsResponse(List<GameMoveResponseDto> list, String message) {
    this.list = list;
    this.message = message;
  }

  public List<GameMoveResponseDto> getList() {
    return list;
  }

  public String getMessage() {
    return message;
  }
}
