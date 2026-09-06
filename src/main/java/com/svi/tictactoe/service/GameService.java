package com.svi.tictactoe.service;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.GameMoveDto;

import java.util.List;
import java.util.UUID;

public interface GameService {
    GameMoveDto saveMove(MoveRequestDto moveRequestDto);
    List<GameMoveDto> getGameDetailsByGameId(UUID id);
    String joinPendingGame(String rawRoomCode, String joiningPlayerName);
    String createPendingGame(String rawRoomCode, String playerName);
    boolean deletePendingGame(String rawRoomCode, String gameId);
}

