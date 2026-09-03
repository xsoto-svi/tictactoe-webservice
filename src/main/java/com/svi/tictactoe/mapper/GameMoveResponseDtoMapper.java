package com.svi.tictactoe.mapper;

import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameMoveResponseDtoMapper {
  public static GameMoveDto fromFileString(String line) {
    String[] parts = line.split(",");

    GameMoveDto move = new GameMoveDto();
    move.setGameId(UUID.fromString(parts[0]));
    move.setPlayerName(parts[1]);
    move.setSymbol(parts[2]);
    move.setLocation(Integer.parseInt(parts[3]));
    move.setDateSaved(LocalDateTime.parse(parts[4]));

    return move;
  }

  public static GameMoveDto toDto(GameMove gameMove) {
    GameMoveDto move = new GameMoveDto();
    move.setGameId(gameMove.getGameId());
    move.setPlayerName(gameMove.getPlayerName());
    move.setSymbol(gameMove.getSymbol());
    move.setLocation(gameMove.getLocation());
    move.setDateSaved(gameMove.getDateSave());

    return move;
  }
}
