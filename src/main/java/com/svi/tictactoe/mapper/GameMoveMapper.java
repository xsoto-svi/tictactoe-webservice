package com.svi.tictactoe.mapper;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.entity.GameMove;
import java.time.LocalDateTime;
import java.util.UUID;

public class GameMoveMapper {
  public static GameMove toEntity(MoveRequestDto dto, String pureGameUuid) {
    GameMove move = new GameMove();

    try{
      move.setGameId(UUID.fromString(pureGameUuid));
      move.setPlayerName(dto.getPlayerName());
      move.setSymbol(dto.getSymbol());
      move.setLocation(dto.getLocation());
      move.setDateSave(LocalDateTime.now());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid UUID format for gameId: " + dto.getGameId());
    }

    return move;
  }
}