package com.svi.tictactoe.repository;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.utils.FileUtil;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

  /* Used for temporary storage of created games */
  public void createPendingGame(String gameId, String roomCode, String playerName) {
    String pendingFileName = roomCode + "_" + gameId + ".txt";
    Path pendingPath = Paths.get(PENDING_DIR, pendingFileName);

    try {
      Files.write(
              pendingPath,
              playerName.getBytes(StandardCharsets.UTF_8),
              StandardOpenOption.CREATE
      );
    } catch (IOException e) {
      throw new RuntimeException("Failed to create pending game", e);
    }
  }

  /* Fetches the game id of a pending room */
  public String getPendingGameId(String roomCode) {
    Path pendingDir = Paths.get(PENDING_DIR);
    if (!Files.exists(pendingDir)) return null;

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(pendingDir, roomCode + "_*.txt")) {
      for (Path path : stream) {
        String fileName = path.getFileName().toString();
        return fileName.substring(roomCode.length() + 1, fileName.length() - 4);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to find pending game for room: " + roomCode, e);
    }
    return null;
  }

  /* Fetches the name of the user that created the pending room */
  public String getPendingGameCreatorName(String roomCode, String gameId) {
    Path pendingPath = Paths.get(PENDING_DIR, roomCode + "_" + gameId + ".txt");
    if (!Files.exists(pendingPath)) return null;

    try (Stream<String> lines = Files.lines(pendingPath)) {
      return lines.findFirst().orElse("");
    } catch (IOException e) {
      throw new RuntimeException("Failed to read creator name for game: " + gameId, e);
    }
  }

  public GameMove saveMoveOnTxtFile(GameMove move) {
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
  public synchronized void addGameIdToPlayer(UUID gameId, String playerName) {
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
  public synchronized void addGameIdToRoomCode(String roomCode, UUID gameId) {
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

  public void deletePendingGame(String roomCode, String gameId) {
    String pendingFileName = roomCode + "_" + gameId + ".txt";

    try {
      Path pendingPath = Paths.get(PENDING_DIR, pendingFileName);
      Files.deleteIfExists(pendingPath);
    } catch (IOException exception) {
      throw new RuntimeException("Failed to delete pending game: " + pendingFileName);
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