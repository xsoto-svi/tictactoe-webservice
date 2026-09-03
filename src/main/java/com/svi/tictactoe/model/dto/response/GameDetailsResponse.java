package com.svi.tictactoe.model.dto.response;

import java.util.List;

public class GameDetailsResponse {
  private final List<GameMoveDto> list;

  private final String message;

  public GameDetailsResponse(List<GameMoveDto> list, String message) {
    this.list = list;
    this.message = message;
  }

  public List<GameMoveDto> getList() {
    return list;
  }

  public String getMessage() {
    return message;
  }
}
