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
    GAME_RECORDS_PATH,
    PLAYER_RECORDS_PATH,
    ROOMS_RECORDS_PATH,
    PENDING_RECORDS_PATH,
    MATCHES_RECORDS_PATH,

    ALLOWED_ORIGINS;

    public String value() {
      return name();
    }
  }
}