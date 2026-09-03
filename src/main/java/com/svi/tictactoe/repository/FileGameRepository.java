package com.svi.tictactoe.repository;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FileGameRepository {

  private static final String GAMES_DIR = AppContextInitializer.GAMES_DIR;
  private static final String PLAYERS_DIR = AppContextInitializer.PLAYERS_DIR;
  private static final String ROOMS_DIR = AppContextInitializer.ROOMS_DIR;

  public GameMove saveMoveOnTxtFile(String roomCode, GameMove move) {
    String gameIdString = move.getGameId().toString();
    Path gamesPath = Paths.get(GAMES_DIR, gameIdString + ".txt");

    String line = String.format("%s,%s,%s,%d,%s%n",
            move.getGameId(),
            move.getPlayerName(),
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

      addGameIdToPlayer(move.getGameId(), move.getPlayerName());
      addGameIdToRoomCode(roomCode, move.getGameId());

      return move;

    } catch (IOException exception) {
      throw new RuntimeException("Failed to save game move to file", exception);
    }
  }

  /* UTIL FUNCTION: Updates player game id list if the game id is new */
  private void addGameIdToPlayer(UUID gameId, String playerName) {
    Path playerPath = Paths.get(PLAYERS_DIR, playerName + ".txt");

    List<UUID> existingGames = getGamesByPlayerName(playerName);
    if (existingGames.contains(gameId)) {
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

  /* UTIL FUNCTION: Associates created games to a specific room code */
  private void addGameIdToRoomCode(String roomCode, UUID gameId) {
    Path roomPath = Paths.get(ROOMS_DIR, roomCode + ".txt");

    List<UUID> existingGames = getGamesByRoomCode(roomCode);
    if (existingGames.contains(gameId)) {
      return;
    }

    String line = gameId + System.lineSeparator();
    try {
      Files.write(
              roomPath,
              line.getBytes(StandardCharsets.UTF_8),
              StandardOpenOption.CREATE,
              StandardOpenOption.APPEND
      );
    } catch (IOException exception) {
      throw new RuntimeException("Failed to update room games list", exception);
    }
  }

  /* UTIL FUNCTION: Gets all the games by specific room code */
  private List<UUID> getGamesByRoomCode(String roomCode) {
    Path filePath = Paths.get(ROOMS_DIR, roomCode + ".txt");

    if (!Files.exists(filePath)) {
      return new ArrayList<>();
    }

    try {
      List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
      return lines.stream()
              .filter(line -> line != null && !line.trim().isEmpty())
              .map(String::trim)
              .map(UUID::fromString)
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read games for room: " + roomCode, e);
    }

  }

  public List<UUID> getGamesByPlayerName(String name) {
    Path filePath = Paths.get(PLAYERS_DIR, name + ".txt");

    if (!Files.exists(filePath)) {
      return new ArrayList<>();
    }

    try {
      List<String> gameIdStrings = Files.readAllLines(filePath, StandardCharsets.UTF_8);

      return gameIdStrings.stream()
              .filter(line -> line != null && !line.trim().isEmpty())
              .map(String::trim)
              .map(UUID::fromString)
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read game list for player: " + name, e);
    }
  }

  public List<GameMoveDto> getGameDetailsByGameId(UUID id) {
    Path filePath = Paths.get(GAMES_DIR, id.toString() + ".txt");

    if (!Files.exists(filePath)) {
      return new ArrayList<>();
    }

    try {
      List<String> gameDetailStrings = Files.readAllLines(filePath, StandardCharsets.UTF_8);

      return gameDetailStrings.stream()
              .filter(line -> line != null && !line.trim().isEmpty())
              .map(String::trim)
              .map(GameMoveResponseDtoMapper::fromFileString)
              .collect(Collectors.toList());

    } catch (IOException e) {
      throw new RuntimeException("Failed to read game list for player: " + id, e);
    }
  }
}