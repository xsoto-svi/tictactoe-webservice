package com.svi.tictactoe.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static final Properties PROPERTIES = new Properties();

  static {
    try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {

      if (input == null) {
        throw new RuntimeException("config.properties not found.");
      }

      PROPERTIES.load(input);

    } catch (IOException e) {
      throw new RuntimeException("Failed to load config.properties.", e);
    }
  }

  private Config() {
  }

  public static String get(String key) {
    String value = PROPERTIES.getProperty(key);

    if (value == null || value.isEmpty()) {
      throw new RuntimeException("Configuration property not found: " + key);
    }

    return value;
  }

  public enum Key {
    ALLOWED_ORIGINS,

    CASSANDRA_IP,
    CASSANDRA_PORT,
    CASSANDRA_DATACENTER,
    CASSANDRA_KEYSPACE,

    ROOM_TABLE,
    GAME_TABLE,
    PENDING_GAME_TABLE,
    PLAYER_TABLE;

    public String value() {
      return name();
    }
  }
}