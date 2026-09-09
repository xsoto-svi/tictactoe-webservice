package com.svi.tictactoe.repository.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.connection.CassandraConnection;
import com.svi.tictactoe.constants.DbConstants;
import com.svi.tictactoe.repository.RoomRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CassandraRoomRepositoryImpl implements RoomRepository {

  private final Session session;

  private final PreparedStatement getRoomCodesStatement;
  private final PreparedStatement getGamesByRoomCodeStatement;
  private final PreparedStatement insertGameToRoomStatement;

  public CassandraRoomRepositoryImpl() {
    String roomTable = Config.Key.ROOM_TABLE.value();

    this.session = CassandraConnection.getInstance().getSession();

    // Prepared once during application startup
    this.getRoomCodesStatement = session.prepare(
            "SELECT DISTINCT room_code FROM " + roomTable
    );
    this.getGamesByRoomCodeStatement = session.prepare(
            "SELECT * FROM " + roomTable + " WHERE room_code = ?"
    );
    this.insertGameToRoomStatement = session.prepare(
            "INSERT INTO " + roomTable + " (room_code, game_id) VALUES (?, ?)"
    );
  }

  @Override
  public List<String> getRoomCodes() {
    ResultSet resultSet = session.execute(getRoomCodesStatement.bind());

    List<String> results = new ArrayList<>();
    for (Row row : resultSet) {
      results.add(row.getString("room_code"));
    }

    return results;
  }

  @Override
  public List<UUID> getGamesByRoomCode(String roomCode) {
    ResultSet resultSet = session.execute(getGamesByRoomCodeStatement.bind(roomCode));

    List<UUID> results = new ArrayList<>();
    for (Row row : resultSet) {
      UUID gameId = row.getUUID("game_id");
      if (gameId != null) {
        results.add(gameId);
      }
    }

    return results;
  }

  @Override
  public void addGameIdToRoomCode(String roomCode, UUID gameId) {
    session.execute(insertGameToRoomStatement.bind(roomCode, gameId));
  }
}