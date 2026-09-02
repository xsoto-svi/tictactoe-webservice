package com.svi.tictactoe.service;

import com.svi.tictactoe.mapper.GameMoveMapper;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.GameMoveResponseDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.FileGameRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class TicTacToeService {

  @Inject
  private FileGameRepository fileGameRepository;

  public void saveMove(MoveRequestDto moveRequestDto) {
    GameMove move = GameMoveMapper.toEntity(moveRequestDto);
    fileGameRepository.saveMoveOnTxtFile(move);
  }

  public List<UUID> getGamesByPlayerName(String name) {
    return fileGameRepository.getGamesByPlayerName(name);
  }

  public List<GameMoveResponseDto> getGameDetailsByGameId(UUID id) {
    return fileGameRepository.getGameDetailsByGameId(id);
  }
}
