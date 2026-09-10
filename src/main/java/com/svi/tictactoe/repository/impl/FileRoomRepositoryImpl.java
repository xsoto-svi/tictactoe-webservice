package com.svi.tictactoe.repository.impl;

import com.svi.tictactoe.config.AppContextInitializer;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.repository.RoomRepository;
import com.svi.tictactoe.utils.FileUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Vetoed
//@ApplicationScoped
public class FileRoomRepositoryImpl implements RoomRepository {

    private static final String ROOMS_DIR = Config.get(Config.Key.ROOM_RECORDS_PATH.value());

    @Override
    public List<String> getRoomCodes() {
        return FileUtil.getFileNamesWithoutExtension(Paths.get(ROOMS_DIR), "Failed to read room directory");
    }

    @Override
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

    @Override
    public synchronized void addGameIdToRoomCode(String roomCode, UUID gameId, String status) {
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

    @Override
    public String getPendingGameId(String roomCode) {
        return null;
    }

    @Override
    public boolean updateRoomStatus(String roomCode, UUID gameId, String status) {
        return false;
    }
}

