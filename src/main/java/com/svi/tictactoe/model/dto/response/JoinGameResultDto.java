package com.svi.tictactoe.model.dto.response;

public class JoinGameResultDto {
  private final String gameId;
  private final String playerName;

  public JoinGameResultDto(String gameId, String playerName) {
    this.gameId = gameId;
    this.playerName = playerName;
  }

  public String getGameId() {
    return gameId;
  }

  public String getPlayerName() {
    return playerName;
  }
}

