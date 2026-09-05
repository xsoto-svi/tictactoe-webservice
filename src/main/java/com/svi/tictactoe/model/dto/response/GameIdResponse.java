package com.svi.tictactoe.model.dto.response;

public class GameIdResponse extends ApiResponse {
  private final String id;

  public GameIdResponse(String message, String id) {
    super(message);
    this.id = id;
  }

  public String getId() {
    return id;
  }
}