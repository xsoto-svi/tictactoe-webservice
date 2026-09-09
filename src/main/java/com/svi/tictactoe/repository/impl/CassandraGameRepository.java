package com.svi.tictactoe.repository.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.connection.CassandraConnection;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.GameRepository;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CassandraGameRepository implements GameRepository {

  private final Session session;

  private final PreparedStatement insertPendingStatement;
  private final PreparedStatement selectPendingIdStatement;
  private final PreparedStatement selectPendingCreatorStatement;
  private final PreparedStatement deletePendingStatement;
  private final PreparedStatement insertMoveStatement;
  private final PreparedStatement selectMovesStatement;

  public CassandraGameRepository() {
    String pendingGameTable = Config.get(Config.Key.PENDING_GAME_TABLE.value());
    String gameTable = Config.get(Config.Key.GAME_TABLE.value());

    this.session = CassandraConnection.getInstance().getSession();

    this.insertPendingStatement = session.prepare(
            "INSERT INTO " + pendingGameTable + " (room_code, game_id, player_name) VALUES (?, ?, ?)"
    );
    this.selectPendingIdStatement = session.prepare(
            "SELECT game_id FROM " + pendingGameTable + " WHERE room_code = ?"
    );
    this.selectPendingCreatorStatement = session.prepare(
            "SELECT player_name FROM " + pendingGameTable + " WHERE room_code = ? AND game_id = ?"
    );
    this.deletePendingStatement = session.prepare(
            "DELETE FROM " + pendingGameTable + " WHERE room_code = ? AND game_id = ? IF EXISTS"
    );

    this.insertMoveStatement = session.prepare(
            "INSERT INTO " + gameTable + " (game_id, date_save, player_name, symbol, location) VALUES (?, ?, ?, ?, ?)"
    );
    this.selectMovesStatement = session.prepare(
            "SELECT * FROM " + gameTable + " WHERE game_id = ?"
    );
  }

  @Override
  public void createPendingGame(String gameId, String roomCode, String playerName) {
    session.execute(insertPendingStatement.bind(roomCode, UUID.fromString(gameId), playerName));
  }

  @Override
  public String getPendingGameId(String roomCode) {
    ResultSet resultSet = session.execute(selectPendingIdStatement.bind(roomCode));
    Row row = resultSet.one();

    if (row != null) {
      UUID gameId = row.getUUID("game_id");
      return gameId != null ? gameId.toString() : null;
    }
    return null;
  }

  @Override
  public String getPendingGameCreatorName(String roomCode, String gameId) {
    ResultSet resultSet = session.execute(selectPendingCreatorStatement.bind(roomCode, UUID.fromString(gameId)));
    Row row = resultSet.one();

    return (row != null) ? row.getString("player_name") : null;
  }

  @Override
  public boolean deletePendingGame(String roomCode, String gameId) {
    ResultSet resultSet = session.execute(deletePendingStatement.bind(roomCode, UUID.fromString(gameId)));
    return resultSet.wasApplied();
  }

  @Override
  public GameMove saveMoveOnTxtFile(GameMove move) {
    // Convert Java LocalDateTime to java.util.Date for Cassandra timestamp column
    Date cassandraTimestamp = Timestamp.valueOf(move.getDateSave());

    session.execute(insertMoveStatement.bind(
            move.getGameId(),
            cassandraTimestamp,
            move.getPlayerName(),
            move.getSymbol(),
            move.getLocation()
    ));

    return move;
  }

  @Override
  public List<GameMove> getGameDetailsByGameId(UUID id) {
    ResultSet resultSet = session.execute(selectMovesStatement.bind(id));

    List<GameMove> moves = new ArrayList<>();
    for (Row row : resultSet) {
      GameMove move = new GameMove();
      move.setGameId(row.getUUID("game_id"));
      move.setPlayerName(row.getString("player_name"));
      move.setSymbol(row.getString("symbol"));
      move.setLocation(row.getInt("location"));

      // Convert Cassandra java.util.Date back to Java LocalDateTime
      Date dateSave = row.getTimestamp("date_save");
      if (dateSave != null) {
        move.setDateSave(new Timestamp(dateSave.getTime()).toLocalDateTime());
      }

      moves.add(move);
    }
    return moves;
  }
}