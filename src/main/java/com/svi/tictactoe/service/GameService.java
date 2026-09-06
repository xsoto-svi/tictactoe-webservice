package com.svi.tictactoe.service;

import com.svi.tictactoe.exceptions.ApiException;
import com.svi.tictactoe.exceptions.InvalidMoveException;
import com.svi.tictactoe.exceptions.PlayerNameAlreadyTakenException;
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
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {

  private FileGameRepository fileGameRepository;

  public GameService() {}

  @Inject
  public GameService(FileGameRepository fileGameRepository) {
    this.fileGameRepository = fileGameRepository;
  }

  public GameMoveDto saveMove(MoveRequestDto moveRequestDto) {
    // Game id here is a composite id that contains <roomcode>_<gameUUID>
    String[] gameInfo = moveRequestDto.getGameId().split("_");
    String gameUuid = gameInfo[1];

    GameMove move = GameMoveMapper.toEntity(moveRequestDto, gameUuid);
    if (!isMoveValid(move)) {
      throw new InvalidMoveException("Location " + move.getLocation() + " is already occupied");
    }

    GameMove savedMove = fileGameRepository.saveMoveOnTxtFile(move);

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

  public List<GameMoveDto> getGameDetailsByGameId(UUID id) {
    return fileGameRepository.getGameDetailsByGameId(id);
  }

  public String joinPendingGame(String rawRoomCode, String joiningPlayerName) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    String gameId = fileGameRepository.getPendingGameId(baseRoomCode);
    if (gameId == null) {
      return null;
    }

    String creatorName = fileGameRepository.getPendingGameCreatorName(baseRoomCode, gameId);

    /* Windows file system is case-insensitive */
    if (joiningPlayerName.equalsIgnoreCase(creatorName)) {
      throw new PlayerNameAlreadyTakenException("Player name '" + joiningPlayerName + "' is already taken in this room.");
    }

    // link both players to this game ID
    fileGameRepository.addGameIdToPlayer(UUID.fromString(gameId), creatorName);
    fileGameRepository.addGameIdToPlayer(UUID.fromString(gameId), joiningPlayerName);

    // link room code to this game ID
    fileGameRepository.addGameIdToRoomCode(baseRoomCode, UUID.fromString(gameId));

    // delete pending game file since the match has officially started
    fileGameRepository.deletePendingGame(baseRoomCode, gameId);

    return gameId;
  }

  public String createPendingGame(String rawRoomCode, String playerName) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    String gameIdString = UUID.randomUUID().toString();
    fileGameRepository.createPendingGame(gameIdString, baseRoomCode, playerName);

    return gameIdString;
  }

  public boolean deletePendingGame(String rawRoomCode, String gameId) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    return fileGameRepository.deletePendingGame(baseRoomCode, gameId);
  }

  /* HELPER FUNCTION: extracts room code from the complete game id and removes 'R's that signifies rematches */
  private String extractBaseRoomCode(String completeGameId) {
    String[] gameInfo = completeGameId.split("_");
    String rawRoomCode = gameInfo[0];

    //On rematch, roomCode gets appended with "R" every time a new match starts
    return rawRoomCode.length() >= 4 ? rawRoomCode.substring(0, 4) : rawRoomCode;
  }
}
