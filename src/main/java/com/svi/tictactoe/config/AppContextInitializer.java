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

    try {
      Path gamesPath = Paths.get(GAMES_DIR);
      if (!Files.exists(gamesPath)) {
        Files.createDirectories(gamesPath);
        LOGGER.info("Successfully created directory: " + GAMES_DIR);
      }

      Path playersPath = Paths.get(PLAYERS_DIR);
      if (!Files.exists(playersPath)) {
        Files.createDirectories(playersPath);
        LOGGER.info("Successfully created directory: " + PLAYERS_DIR);
      }

      Path roomsPath = Paths.get(ROOMS_DIR);
      if (!Files.exists(roomsPath)) {
        Files.createDirectories(roomsPath);
        LOGGER.info("Successfully created directory: " + ROOMS_DIR);
      }

      Path pendingPath = Paths.get(PENDING_DIR);
      if (!Files.exists(pendingPath)) {
        Files.createDirectories(pendingPath);
        LOGGER.info("Successfully created directory: " + ROOMS_DIR);
      }

    } catch (IOException e) {
      LOGGER.severe("Failed to initialize storage directories: " + e.getMessage());
      throw new RuntimeException("Application startup initialization failed", e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    LOGGER.info(">>> Tic-Tac-Toe Application is shutting down.");
  }
}