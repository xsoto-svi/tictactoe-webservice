package com.svi.tictactoe.service;

import com.svi.tictactoe.mapper.GameMoveMapper;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.FileGameRepository;

import javax.inject.Inject;

public class TicTacToeService {

  @Inject
  private FileGameRepository fileGameRepository;

  public String saveMoveOnTxtFile(MoveRequestDto moveRequestDto) {
    GameMove move = GameMoveMapper.toEntity(moveRequestDto);
    fileGameRepository.saveMoveOnTxtFile(move);

    return "Record saved.";
  }

//  public ListGamesResponseDto listGames() {
//    return new ListGamesResponseDto();
//  }
}
