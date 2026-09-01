package com.svi.tictactoe.model.dto.response;

import java.util.List;

public class ListGamesResponseDto {
  private List<GameItemDto> list;
  private String message;

  public ListGamesResponseDto(List<GameItemDto> list, String message) {
    this.list = list;
    this.message = message;
  }

  public static class GameItemDto {
    private String id;

    public GameItemDto(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
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
