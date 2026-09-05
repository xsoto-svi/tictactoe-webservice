package com.svi.tictactoe.repository;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.utils.FileUtil;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class FileGameRepository {

  private static final String GAMES_DIR = AppContextInitializer.GAMES_DIR;
  private static final String PLAYERS_DIR = AppContextInitializer.PLAYERS_DIR;
  private static final String ROOMS_DIR = AppContextInitializer.ROOMS_DIR;
  private static final String PENDING_DIR = AppContextInitializer.PENDING_DIR;

  public List<String> getPlayerNames() {
    return getFileNames(PLAYERS_DIR, "Failed to read players directory");
  }

  public List<String> getRoomCodes() {
    return getFileNames(ROOMS_DIR, "Failed to read room directory");
  }

  /* Used for fetching all active rooms waiting for joiners */
  public List<String> getPendingGames() {
    return getFileNames(PENDING_DIR, "Failed to read 'pending' directory");
  }

  /* Used for temporary storage of created games */
  public void createPendingGame(String gameId, String roomCode) {
    String pendingFileName = roomCode + "_" + gameId + ".txt";
    Path pendingPath = Paths.get(PENDING_DIR, pendingFileName);

    try {
      Files.write(
              pendingPath,
              new byte[0],
              StandardOpenOption.CREATE
      );
    } catch (IOException e) {
      throw new RuntimeException("Failed to create pending game", e);
    }
  }

  public GameMove saveMoveOnTxtFile(String roomCode, GameMove move) {
    String gameIdString = move.getGameId().toString();
    Path gamesPath = Paths.get(GAMES_DIR, gameIdString + ".txt");

    boolean isFirstMove = !Files.exists(gamesPath);

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

      // Prevents multiple writes of the same gameid
      if (isFirstMove) {
        addGameIdToPlayer(move.getGameId(), move.getPlayerName());
        addGameIdToRoomCode(roomCode, move.getGameId());

        // Destroy pending game if game already started
        Path pendingPath = Paths.get(PENDING_DIR, roomCode + "_" + gameIdString + ".txt");
        Files.deleteIfExists(pendingPath);
      }

      return move;

    } catch (IOException exception) {
      throw new RuntimeException("Failed to save game move to file", exception);
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

  public List<UUID> getGamesByRoomCode(String roomCode) {
    Path filePath = Paths.get(ROOMS_DIR, roomCode + ".txt");

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
      throw new RuntimeException("Failed to read game list for room: " + roomCode, e);
    }
  }

  /* HELPER FUNCTION: Updates player game id list */
  private void addGameIdToPlayer(UUID gameId, String playerName) {
    Path playerPath = Paths.get(PLAYERS_DIR, playerName + ".txt");
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

  /* HELPER FUNCTION: Associates created games to a specific room code */
  private void addGameIdToRoomCode(String roomCode, UUID gameId) {
    Path roomPath = Paths.get(ROOMS_DIR, roomCode + ".txt");
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

  /* HELPER FUNCTION: returns list of file names of specified directory */
  private List<String> getFileNames(String directory, String errorMessage) {
    Path directoryPath = Paths.get(directory);

    if (!Files.exists(directoryPath)) {
      return Collections.emptyList();
    }

    try (Stream<Path> paths = Files.list(directoryPath)) {
      return paths
              .filter(Files::isRegularFile)
              .filter(FileUtil::isTxtFile)
              .map(path -> {
                String name = path.getFileName().toString();
                return name.substring(0, name.length() - 4);
              })
              .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException(errorMessage, e);
    }
  }
}