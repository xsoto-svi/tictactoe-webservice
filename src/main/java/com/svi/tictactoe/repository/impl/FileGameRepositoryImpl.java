package com.svi.tictactoe.repository.impl;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.GameRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class FileGameRepositoryImpl implements GameRepository {

    private static final String GAMES_DIR = AppContextInitializer.GAMES_DIR;
    private static final String PENDING_DIR = AppContextInitializer.PENDING_DIR;

    @Override
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

    @Override
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

    @Override
    public String getPendingGameCreatorName(String roomCode, String gameId) {
        Path pendingPath = Paths.get(PENDING_DIR, roomCode + "_" + gameId + ".txt");
        if (!Files.exists(pendingPath)) return null;

        try (Stream<String> lines = Files.lines(pendingPath)) {
            return lines.findFirst().orElse("");
        } catch (IOException e) {
            throw new RuntimeException("Failed to read creator name for game: " + gameId, e);
        }
    }

    @Override
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

    @Override
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

    @Override
    public boolean deletePendingGame(String roomCode, String gameId) {
        String pendingFileName = roomCode + "_" + gameId + ".txt";

        try {
            Path pendingPath = Paths.get(PENDING_DIR, pendingFileName);
            return Files.deleteIfExists(pendingPath);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to delete pending game: " + pendingFileName);
        }
    }
}

