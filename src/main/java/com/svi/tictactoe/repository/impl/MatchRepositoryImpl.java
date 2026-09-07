package com.svi.tictactoe.repository.impl;

import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.repository.MatchRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@ApplicationScoped
public class MatchRepositoryImpl implements MatchRepository {

  private static final Logger LOGGER = Logger.getLogger(MatchRepositoryImpl.class.getName());
  private static final String MATCHES_DIR = Config.get(Config.Key.MATCHES_RECORDS_PATH.value());

  @Override
  public void createMatch(String gameId, String player1Name, String player2Name) {
    Path path = Paths.get(MATCHES_DIR, gameId + ".txt");
    try {
      Files.write(path, (player1Name + "," + player2Name).getBytes());
    } catch (IOException e) {
      LOGGER.severe("Failed to create match file for gameId: " + gameId);
      throw new RuntimeException("Failed to create match file", e);
    }
  }

  @Override
  public String getMatch(String gameId) {
    Path path = Paths.get(MATCHES_DIR, gameId + ".txt");
    if (!Files.exists(path)) {
      return null;
    }
    try {
      return new String(Files.readAllBytes(path));
    } catch (IOException e) {
      LOGGER.severe("Failed to read match file for gameId: " + gameId);
      return null;
    }
  }
}

