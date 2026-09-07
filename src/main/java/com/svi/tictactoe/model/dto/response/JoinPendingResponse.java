package com.svi.tictactoe.model.dto.response;

public class JoinPendingResponse extends ApiResponse {
  private final String id;
  private final String playerName;

  public JoinPendingResponse(String message, String id, String playerName) {
    super(message);
    this.id = id;
    this.playerName = playerName;
  }

  public String getId() {
    return id;
  }

  public String getPlayerName() {
    return playerName;
  }
}

