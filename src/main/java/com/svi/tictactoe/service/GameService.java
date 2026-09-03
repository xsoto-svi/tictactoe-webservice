package com.svi.tictactoe.service;

import com.svi.tictactoe.mapper.GameMoveMapper;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.GameMoveResponseDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.FileGameRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GameService {

  @Inject
  private FileGameRepository fileGameRepository;

  public GameMoveResponseDto saveMove(MoveRequestDto moveRequestDto) {
    String[] gameInfo = moveRequestDto.getGameId().split("_");
    String rawRoomCode = gameInfo[0];
    String gameUuid = gameInfo[1];

    //On rematch, roomCode gets appended with "R" every time a new match starts
    String baseRoomCode = rawRoomCode.length() >= 4 ? rawRoomCode.substring(0, 4) : rawRoomCode;
    moveRequestDto.setGameId(gameUuid);

    GameMove move = GameMoveMapper.toEntity(moveRequestDto);
    GameMove savedMove = fileGameRepository.saveMoveOnTxtFile(baseRoomCode, move);

    return GameMoveResponseDtoMapper.toDto(savedMove);
  }

  public List<UUID> getGamesByPlayerName(String name) {
    return fileGameRepository.getGamesByPlayerName(name);
  }

  public List<GameMoveResponseDto> getGameDetailsByGameId(UUID id) {
    return fileGameRepository.getGameDetailsByGameId(id);
  }
}
