package com.svi.tictactoe.repository.impl;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.svi.tictactoe.config.CassandraManager;
import com.svi.tictactoe.constants.DbConstants;
import com.svi.tictactoe.repository.RoomRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CassandraRoomRepositoryImpl implements RoomRepository {

  private static final String GET_ROOM_CODES = "SELECT DISTINCT (room_code) FROM " + DbConstants.ROOM_TABLE;
  private static final String GET_GAMES_BY_ROOM_CODE = "SELECT * FROM " + DbConstants.ROOM_TABLE + " WHERE room_code = ?";
  private static final String INSERT_GAME_TO_ROOM = "INSERT INTO " + DbConstants.ROOM_TABLE + "(room_code, game_id) VALUES (?, ?)";

  @Override
  public List<String> getRoomCodes() {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(GET_ROOM_CODES);
    ResultSet resultSet = session.execute(prepared.bind());

    List<String> results = new ArrayList<>();

    for (Row row : resultSet) {
      results.add(row.getString("room_code"));
    }

    return results;
  }

  @Override
  public List<UUID> getGamesByRoomCode(String roomCode) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(GET_GAMES_BY_ROOM_CODE);
    ResultSet resultSet = session.execute(prepared.bind(roomCode));

    List<UUID> results = new ArrayList<>();

    for (Row row : resultSet) {
      UUID gameId = row.getUuid("game_id");
      if (gameId != null) {
        results.add(gameId);
      }
    }

    return results;
  }

  @Override
  public UUID addGameIdToRoomCode(String roomCode, UUID gameId) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(INSERT_GAME_TO_ROOM);

    session.execute(prepared.bind(roomCode, gameId));

    return gameId;
  }
}

