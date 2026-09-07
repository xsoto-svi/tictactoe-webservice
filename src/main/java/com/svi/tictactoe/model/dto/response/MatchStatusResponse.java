package com.svi.tictactoe.model.dto.response;

public class MatchStatusResponse {
  private final String status;
  private final String opponentName;

  public MatchStatusResponse(String status, String opponentName) {
    this.status = status;
    this.opponentName = opponentName;
  }

  public String getStatus() {
    return status;
  }

  public String getOpponentName() {
    return opponentName;
  }
}

