package com.svi.tictactoe.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@WebListener
public class AppContextInitializer implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(AppContextInitializer.class.getName());

  private static final String GAMES_DIR = Config.get(Config.Key.GAME_RECORDS_PATH.value());
  private static final String PLAYERS_DIR = Config.get(Config.Key.PLAYER_RECORDS_PATH.value());
  private static final String ROOMS_DIR = Config.get(Config.Key.ROOMS_RECORDS_PATH.value());
  private static final String PENDING_DIR = Config.get(Config.Key.PENDING_RECORDS_PATH.value());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    LOGGER.info(">>> Tic-Tac-Toe Application is starting up...");
    LOGGER.info(">>> Connecting to Cassandra...");

    try {
      CassandraManager.connectAndInitialize();
      LOGGER.info(">>> Cassandra initialized successfully.");
    } catch (Exception e) {
      LOGGER.severe(">>> Failed to connect to Cassandra: " + e.getMessage());
      throw new RuntimeException("DB Initialization failed", e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    LOGGER.info(">>> Shutting down app, closing DB connection.");
    CassandraManager.close();
  }
}