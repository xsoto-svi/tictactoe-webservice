package com.svi.tictactoe.repository.impl;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.svi.tictactoe.config.CassandraManager;
import com.svi.tictactoe.constants.DbConstants;
import com.svi.tictactoe.model.entity.GameMove;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CassandraGameRepository {

  // --- PENDING GAME QUERIES ---
  private static final String INSERT_PENDING = "INSERT INTO " + DbConstants.PENDING_GAME_TABLE + " (room_code, game_id, player_name) VALUES (?, ?, ?)";
  private static final String SELECT_PENDING_ID = "SELECT game_id FROM " + DbConstants.PENDING_GAME_TABLE + " WHERE room_code = ?";
  private static final String SELECT_PENDING_CREATOR = "SELECT player_name FROM " + DbConstants.PENDING_GAME_TABLE + " WHERE room_code = ? AND game_id = ?";

  // IF EXISTS allows us to know if the row was actually there to be deleted
  private static final String DELETE_PENDING = "DELETE FROM " + DbConstants.PENDING_GAME_TABLE + " WHERE room_code = ? AND game_id = ? IF EXISTS";

  // --- GAME MOVE QUERIES ---
  private static final String INSERT_MOVE = "INSERT INTO " + DbConstants.GAME_TABLE + " (game_id, date_save, player_name, symbol, location) VALUES (?, ?, ?, ?, ?)";
  private static final String SELECT_MOVES = "SELECT * FROM " + DbConstants.GAME_TABLE + " WHERE game_id = ?";

  public void createPendingGame(String gameId, String roomCode, String playerName) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(INSERT_PENDING);
    session.execute(prepared.bind(roomCode, gameId, playerName));
  }

  public String getPendingGameId(String roomCode) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(SELECT_PENDING_ID);
    ResultSet resultSet = session.execute(prepared.bind(roomCode));

    Row row = resultSet.one();
    return (row != null) ? row.getString("game_id") : null;
  }

  String getPendingGameCreatorName(String roomCode, String gameId) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(SELECT_PENDING_CREATOR);
    ResultSet resultSet = session.execute(prepared.bind(roomCode, gameId));

    Row row = resultSet.one();
    return (row != null) ? row.getString("player_name") : null;
  }

  boolean deletePendingGame(String roomCode, String gameId) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(DELETE_PENDING);
    ResultSet resultSet = session.execute(prepared.bind(roomCode, gameId));

    // Returns true if the row existed and was deleted, matching Files.deleteIfExists()
    return resultSet.wasApplied();
  }

  GameMove saveMoveOnTxtFile(GameMove move) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(INSERT_MOVE);

    session.execute(prepared.bind(
            move.getGameId(),
            move.getDateSave().toString(), // Convert LocalDateTime to ISO-8601 String
            move.getPlayerName(),
            move.getSymbol(),
            move.getLocation()
    ));

    return move;
  }

  List<GameMove> getGameDetailsByGameId(UUID id){
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(SELECT_MOVES);
    ResultSet resultSet = session.execute(prepared.bind(id));

    List<GameMove> moves = new ArrayList<>();
    for (Row row : resultSet) {
      GameMove move = new GameMove();
      move.setGameId(row.getUuid("game_id"));
      move.setPlayerName(row.getString("player_name"));
      move.setSymbol(row.getString("symbol"));
      move.setLocation(row.getInt("location"));

      String dateSaveStr = row.getString("date_save");
      if (dateSaveStr != null) {
        move.setDateSave(LocalDateTime.parse(dateSaveStr));
      }

      moves.add(move);
    }
    return moves;
  }
}
