package com.svi.tictactoe.repository.impl;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.svi.tictactoe.config.CassandraManager;
import com.svi.tictactoe.constants.DbConstants;
import com.svi.tictactoe.repository.PlayerRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CassandraPlayerRepository implements PlayerRepository {

  private static final String GET_PLAYER_NAMES = "SELECT DISTINCT (player_name) FROM " + DbConstants.PLAYER_TABLE;
  private static final String GET_PLAYER_NAMES_BY_ROOM_CODE = "SELECT * FROM " + DbConstants.PLAYER_TABLE + " WHERE player_name = ?";
  private static final String INSERT_GAME_TO_ROOM = "INSERT INTO " + DbConstants.PLAYER_TABLE + " (player_name, game_id) VALUES (?, ?)";

  @Override
  public List<String> getPlayerNames() {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(GET_PLAYER_NAMES);
    ResultSet resultSet = session.execute(prepared.bind());

    List<String> results = new ArrayList<>();

    for (Row row : resultSet) {
      results.add(row.getString("player_name"));
    }

    return results;
  }

  @Override
  public List<UUID> getGamesByPlayerName(String name) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(GET_PLAYER_NAMES_BY_ROOM_CODE);
    ResultSet resultSet = session.execute(prepared.bind(name));

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
  public void addGameIdToPlayer(UUID gameId, String playerName) {
    CqlSession session = CassandraManager.getSession();
    PreparedStatement prepared = session.prepare(INSERT_GAME_TO_ROOM);

    session.execute(prepared.bind(playerName, gameId));
  }
}

