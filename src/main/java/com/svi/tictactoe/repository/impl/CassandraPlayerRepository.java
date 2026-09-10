package com.svi.tictactoe.repository.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.connection.CassandraConnection;
import com.svi.tictactoe.repository.PlayerRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CassandraPlayerRepository implements PlayerRepository {

  private final Session session;

  private final PreparedStatement getPlayerNamesStatement;
  private final PreparedStatement getGamesByPlayerNameStatement;
  private final PreparedStatement insertGameToPlayerStatement;

  public CassandraPlayerRepository() {
    String playerTable = Config.get(Config.Key.PLAYER_TABLE.value());

    this.session = CassandraConnection.getInstance().getSession();

    this.getPlayerNamesStatement = session.prepare(
            "SELECT DISTINCT player_name FROM " + playerTable
    );
    this.getGamesByPlayerNameStatement = session.prepare(
            "SELECT * FROM " + playerTable + " WHERE player_name = ?"
    );
    this.insertGameToPlayerStatement = session.prepare(
            "INSERT INTO " + playerTable + " (player_name, game_id, room_code) VALUES (?, ?, ?)"
    );
  }

  @Override
  public List<String> getPlayerNames() {
    ResultSet resultSet = session.execute(getPlayerNamesStatement.bind());

    List<String> results = new ArrayList<>();
    for (Row row : resultSet) {
      results.add(row.getString("player_name"));
    }

    return results;
  }

  @Override
  public List<UUID> getGamesByPlayerName(String name) {
    ResultSet resultSet = session.execute(getGamesByPlayerNameStatement.bind(name));

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
  public void addGameIdToPlayer(UUID gameId, String playerName, String roomCode) {
    session.execute(insertGameToPlayerStatement.bind(playerName, gameId, roomCode));
  }

  @Override
  public String getCreatorByRoomAndGame(String roomCode, UUID gameId) {
    ResultSet resultSet = session.execute(getPlayerNamesStatement.bind());
    for (Row row : resultSet) {
      String playerName = row.getString("player_name");
      List<UUID> games = getGamesByPlayerName(playerName);
      if (games.contains(gameId)) {
        return playerName;
      }
    }
    return null;
  }
}