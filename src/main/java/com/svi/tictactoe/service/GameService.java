package com.svi.tictactoe.service;

import com.svi.tictactoe.exceptions.InvalidMoveException;
import com.svi.tictactoe.mapper.GameMoveMapper;
import com.svi.tictactoe.mapper.GameMoveResponseDtoMapper;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.FileGameRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {

  private final FileGameRepository fileGameRepository;

  @Inject
  public GameService(FileGameRepository fileGameRepository) {
    this.fileGameRepository = fileGameRepository;
  }

  public GameMoveDto saveMove(MoveRequestDto moveRequestDto) {
    String[] gameInfo = moveRequestDto.getGameId().split("_");
    String rawRoomCode = gameInfo[0];
    String gameUuid = gameInfo[1];

    //On rematch, roomCode gets appended with "R" every time a new match starts
    String baseRoomCode = rawRoomCode.length() >= 4 ? rawRoomCode.substring(0, 4) : rawRoomCode;

    GameMove move = GameMoveMapper.toEntity(moveRequestDto, gameUuid);
    if (!isMoveValid(move)) {
      throw new InvalidMoveException("Location " + move.getLocation() + " is already occupied");
    }

    GameMove savedMove = fileGameRepository.saveMoveOnTxtFile(baseRoomCode, move);

    return GameMoveResponseDtoMapper.toDto(savedMove);
  }

  private boolean isMoveValid(GameMove newMove) {
    UUID gameId = newMove.getGameId();
    List<GameMoveDto> gameMoves = getGameDetailsByGameId(gameId);

    for (GameMoveDto gameMove : gameMoves) {
      int currLocation = gameMove.getLocation();
      int newMoveLocation = newMove.getLocation();

      if (currLocation == newMoveLocation) {
        return false;
      }
    }

    return true;
  }

  public List<JsonObject> getGamesByRoomCode(String roomCode) {
    return fileGameRepository.getAllGamesByRoomCode(roomCode).stream()
            .map(id -> Json.createObjectBuilder()
                    .add("id", id.toString())
                    .build())
            .collect(Collectors.toList());
  }

  public List<GameMoveDto> getGameDetailsByGameId(UUID id) {
    return fileGameRepository.getGameDetailsByGameId(id);
  }
}
