package com.svi.tictactoe.connection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.Config;

import javax.enterprise.context.ApplicationScoped;

public class CassandraConnection implements AutoCloseable {

  private final Cluster cluster;
  private final Session session;

  public CassandraConnection() {
    String host = Config.get(Config.Key.CASSANDRA_IP.value());
    int port = Integer.parseInt(Config.get(Config.Key.CASSANDRA_PORT.value()));

    this.cluster = Cluster.builder()
            .addContactPoint(host)
            .withPort(port)
            .build();

    this.session = cluster.connect();
  }

  private static final class ConnectionHolder {
    private static final CassandraConnection INSTANCE = new CassandraConnection();
  }

  public static CassandraConnection getInstance() {
    return ConnectionHolder.INSTANCE;
  }

  public void initializeTables() {

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_room_table (" +
            "room_code text, " +
            "game_id uuid, " +
            "PRIMARY KEY (room_code, game_id));");

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_player_table (" +
            "player_name text, " +
            "game_id uuid, " +
            "PRIMARY KEY (player_name, game_id));");

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_game_table (" +
            "game_id uuid, " +
            "player_name text, " +
            "symbol text, " +
            "location int, " +
            "date_save timestamp, " +
            "PRIMARY KEY (game_id, date_save)) " +
            "WITH CLUSTERING ORDER BY (date_save ASC);");

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_pending_game_table (" +
            "room_code text, " +
            "game_id uuid, " +
            "player_name text, " +
            "PRIMARY KEY (room_code, game_id));");
  }

  public Session getSession() {
    return session;
  }

  public Cluster getCluster() {
    return cluster;
  }

  @Override
  public void close() {
    if (session != null && !session.isClosed()) {
      session.close();
    }
    if (cluster != null && !cluster.isClosed()) {
      cluster.close();
    }
  }
}