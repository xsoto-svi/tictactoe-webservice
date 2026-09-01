package com.svi.tictactoe.repository;

import com.svi.tictactoe.model.entity.GameMove;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FileGameRepository {

  private static final String GAMES_DIR = "data/games";
  private static final String PLAYERS_DIR = "data/players";

  public void saveMoveOnTxtFile(GameMove move) {
    String gameIdString = move.getGameId().toString();
    Path gamesPath = Paths.get(GAMES_DIR, gameIdString + ".txt");

    String line = String.format("%s,%s,%s,%d,%s%n",
            move.getGameId(),
            move.getPlayerId(),
            move.getSymbol(),
            move.getLocation(),
            move.getDateSave()
    );

    try {
      Files.write(
              gamesPath,
              line.getBytes(StandardCharsets.UTF_8),
              StandardOpenOption.CREATE,
              StandardOpenOption.APPEND
      );

      addGameIdToPlayer(move.getGameId(), move.getPlayerId());

    } catch (IOException exception) {
      throw new RuntimeException("Failed to save game move to file", exception);
    }
  }

  /* Updates player game id list if the game id is new */
  private void addGameIdToPlayer(UUID gameId, UUID playerId) {
    String playerIdString = playerId.toString();
    Path playerPath = Paths.get(PLAYERS_DIR, playerIdString + ".txt");

    List<String> existingGames = getGamesByPlayer(playerId);
    if (existingGames.contains(gameId.toString())) {
      return;
    }

    String line = gameId + System.lineSeparator();
    try {
      Files.write(
              playerPath,
              line.getBytes(StandardCharsets.UTF_8),
              StandardOpenOption.CREATE,
              StandardOpenOption.APPEND
      );
    } catch (IOException exception) {
      throw new RuntimeException("Failed to update player games list", exception);
    }
  }

  public List<String> getGamesByPlayer(UUID id) {
    List<String> gameIds = new ArrayList<>();

    Path filePath = Paths.get(PLAYERS_DIR, id.toString() + ".txt");

    if (!Files.exists(filePath)) {
      return gameIds;
    }

    try {
      gameIds = Files.readAllLines(filePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read game list for player: " + id, e);
    }

    return gameIds;
  }

  public List<GameMove> getGameDetailsByGameId(UUID id) {
    List<GameMove> gameDetails = new ArrayList<>();

    Path filePath = Paths.get(PLAYERS_DIR, id.toString() + ".txt");

    if (!Files.exists(filePath)) {
      return gameDetails;
    }

    try {
      gameDetails = Files.readAllLines(filePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read game list for player: " + id, e);
    }

    return gameDetails;
  }
}