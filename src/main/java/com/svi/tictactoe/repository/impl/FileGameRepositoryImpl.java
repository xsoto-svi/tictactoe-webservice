package com.svi.tictactoe.repository.impl;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.GameRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Vetoed
//@ApplicationScoped
public class FileGameRepositoryImpl implements GameRepository {

    private static final String GAMES_DIR = Config.get(Config.Key.GAME_RECORDS_PATH.value());
    private static final String PENDING_DIR = Config.get(Config.Key.PENDING_RECORDS_PATH.value());

    @Override
    public GameMove saveMove(GameMove move) {
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
    public List<GameMove> getGameDetailsByGameId(UUID id) {
        Path filePath = Paths.get(GAMES_DIR, id.toString() + ".txt");

        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> gameDetailStrings = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            return gameDetailStrings.stream()
                    .filter(line -> line != null && !line.trim().isEmpty())
                    .map(String::trim)
                    .map(this::fromFileString)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Failed to read game list for player: " + id, e);
        }
    }

    private GameMove fromFileString(String line) {
        String[] parts = line.split(",");
        GameMove move = new GameMove();
        move.setGameId(UUID.fromString(parts[0]));
        move.setPlayerName(parts[1]);
        move.setSymbol(parts[2]);
        move.setLocation(Integer.parseInt(parts[3]));
        move.setDateSave(java.time.LocalDateTime.parse(parts[4]));
        return move;
    }
}

