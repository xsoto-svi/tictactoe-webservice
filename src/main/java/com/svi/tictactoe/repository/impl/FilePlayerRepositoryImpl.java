package com.svi.tictactoe.repository.impl;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.repository.PlayerRepository;
import com.svi.tictactoe.utils.FileUtil;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class FilePlayerRepositoryImpl implements PlayerRepository {

    private static final String PLAYERS_DIR = AppContextInitializer.PLAYERS_DIR;

    @Override
    public List<String> getPlayerNames() {
        return FileUtil.getFileNamesWithoutExtension(Paths.get(PLAYERS_DIR), "Failed to read players directory");
    }

    @Override
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

    @Override
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
}

