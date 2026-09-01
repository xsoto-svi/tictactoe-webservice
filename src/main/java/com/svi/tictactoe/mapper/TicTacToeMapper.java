package com.svi.tictactoe.mapper;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.entity.GameMove;
import java.time.LocalDateTime;
import java.util.UUID;

public class TicTacToeMapper {
  public static GameMove toEntity(MoveRequestDto dto) {
    GameMove move = new GameMove();

    try{
      move.setGameId(UUID.fromString(dto.getGameId()));
      move.setPlayerId(UUID.fromString(dto.getPlayerId()));
      move.setSymbol(dto.getSymbol());
      move.setLocation(dto.getLocation());
      move.setDateSave(LocalDateTime.now());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid UUID format for gameId: " + dto.getGameId());
    }

    return move;
  }
}