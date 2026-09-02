package com.svi.tictactoe.model.dto.response;

import java.util.List;
import java.util.UUID;

public class ListGamesResponse {
  private List<GameItemDto> list;
  private String message;

  public ListGamesResponse(List<GameItemDto> list, String message) {
    this.list = list;
    this.message = message;
  }

  public static class GameItemDto {
    private final UUID id;

    public GameItemDto(UUID id) {
      this.id = id;
    }

    public UUID getId() {
      return id;
    }
  }

  public List<GameItemDto> getList() {
    return list;
  }

  public void setList(List<GameItemDto> list) {
    this.list = list;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
