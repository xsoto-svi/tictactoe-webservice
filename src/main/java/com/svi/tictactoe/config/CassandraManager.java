package com.svi.tictactoe.config;

import com.datastax.oss.driver.api.core.CqlSession;
import java.net.InetSocketAddress;

public class CassandraManager {

  private static CqlSession session;

  public static void connectAndInitialize() {
    String host = Config.get(Config.Key.CASSANDRA_HOST.value());
    int port = Integer.parseInt(Config.get(Config.Key.CASSANDRA_PORT.value()));
    String datacenter = Config.get(Config.Key.CASSANDRA_DATACENTER.value());

    session = CqlSession.builder()
            .addContactPoint(new InetSocketAddress(host, port))
            .withLocalDatacenter(datacenter)
            .build();

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_room_table (" +
            "room_code text, " +
            "game_id uuid, " +
            "PRIMARY KEY (room_code));");

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_player_table (" +
            "player_name text, " +
            "game_id uuid, " +
            "PRIMARY KEY (player_name));");

    session.execute("CREATE TABLE IF NOT EXISTS batch1_2026_trainees.soto_game_table (" +
            "game_id uuid, " +
            "player_name text, " +
            "symbol text, " +
            "location int, " +
            "date_save timestamp, " +
            "PRIMARY KEY (game_id));");
  }

  public static CqlSession getSession() {
    return session;
  }

  public static void close() {
    if (session != null) {
      session.close();
    }
  }
}