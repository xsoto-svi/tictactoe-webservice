package com.svi.tictactoe.repository;

import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;

import java.util.List;
import java.util.UUID;

public interface GameRepository {
    void createPendingGame(String gameId, String roomCode, String playerName);
    String getPendingGameId(String roomCode);
    String getPendingGameCreatorName(String roomCode, String gameId);
    boolean deletePendingGame(String roomCode, String gameId);
    GameMove saveMoveOnTxtFile(GameMove move);
    List<GameMoveDto> getGameDetailsByGameId(UUID id);
}

