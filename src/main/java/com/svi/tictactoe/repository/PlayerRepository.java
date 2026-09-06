package com.svi.tictactoe.repository;

import java.util.List;
import java.util.UUID;

public interface PlayerRepository {
    List<String> getPlayerNames();
    List<UUID> getGamesByPlayerName(String name);
    void addGameIdToPlayer(UUID gameId, String playerName);
}

