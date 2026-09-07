package com.svi.tictactoe.repository;

public interface MatchRepository {
  void createMatch(String gameId, String player1Name, String player2Name);
  String getMatch(String gameId);
}

