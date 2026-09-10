package com.svi.tictactoe.repository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository {
    List<String> getRoomCodes();
    List<UUID> getGamesByRoomCode(String roomCode);
    void addGameIdToRoomCode(String roomCode, UUID gameId, String status);
    String getPendingGameId(String roomCode);
    boolean updateRoomStatus(String roomCode, UUID gameId, String status);
}

