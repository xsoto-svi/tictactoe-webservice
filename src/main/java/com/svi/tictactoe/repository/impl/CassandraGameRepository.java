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

  private final PreparedStatement insertMoveStatement;
  private final PreparedStatement selectMovesStatement;

  public CassandraGameRepository() {
    String gameTable = Config.get(Config.Key.GAME_TABLE.value());

    this.session = CassandraConnection.getInstance().getSession();

    this.insertMoveStatement = session.prepare(
            "INSERT INTO " + gameTable + " (game_id, date_save, player_name, symbol, location) VALUES (?, ?, ?, ?, ?)"
    );
    this.selectMovesStatement = session.prepare(
            "SELECT * FROM " + gameTable + " WHERE game_id = ?"
    );
  }

  @Override
  public GameMove saveMove(GameMove move) {
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