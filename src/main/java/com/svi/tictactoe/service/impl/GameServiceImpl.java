package com.svi.tictactoe.service.impl;

import com.svi.tictactoe.exceptions.InvalidMoveException;
import com.svi.tictactoe.exceptions.PlayerNameAlreadyTakenException;
import com.svi.tictactoe.constants.ErrorMessage;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.GameMoveDto;
import com.svi.tictactoe.model.entity.GameMove;
import com.svi.tictactoe.repository.GameRepository;
import com.svi.tictactoe.repository.PlayerRepository;
import com.svi.tictactoe.repository.RoomRepository;
import com.svi.tictactoe.service.GameService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameServiceImpl implements GameService {

  private GameRepository gameRepository;
  private PlayerRepository playerRepository;
  private RoomRepository roomRepository;

  public GameServiceImpl() {}

  @Inject
  public GameServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository, RoomRepository roomRepository) {
    this.gameRepository = gameRepository;
    this.playerRepository = playerRepository;
    this.roomRepository = roomRepository;
  }

  @Override
  public GameMoveDto saveMove(MoveRequestDto moveRequestDto) {
    // Game id here is a composite id that contains <roomcode>_<gameUUID>
    String[] gameInfo = moveRequestDto.getGameId().split("_");
    String gameUuid = gameInfo[1];

    GameMove move = toEntity(moveRequestDto, gameUuid);
    if (!isMoveValid(move)) {
      throw new InvalidMoveException(ErrorMessage.LOCATION_OCCUPIED.formatMessage(move.getLocation()));
    }

    GameMove savedMove = gameRepository.saveMoveOnTxtFile(move);

    return toDto(savedMove);
  }

  private boolean isMoveValid(GameMove newMove) {
    UUID gameId = newMove.getGameId();
    List<GameMove> gameMoves = gameRepository.getGameDetailsByGameId(gameId);

    return gameMoves.stream()
            .map(GameMove::getLocation)
            .noneMatch(loc -> loc == newMove.getLocation());
  }

  @Override
  public List<GameMoveDto> getGameDetailsByGameId(UUID id) {
    return gameRepository.getGameDetailsByGameId(id).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public String joinPendingGame(String rawRoomCode, String joiningPlayerName) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    String gameId = gameRepository.getPendingGameId(baseRoomCode);
    if (gameId == null) {
      return null;
    }

    String creatorName = gameRepository.getPendingGameCreatorName(baseRoomCode, gameId);

    /* Windows file system is case-insensitive */
    if (joiningPlayerName.equalsIgnoreCase(creatorName)) {
      throw new PlayerNameAlreadyTakenException(joiningPlayerName);
    }

    // link both players to this game ID
    playerRepository.addGameIdToPlayer(UUID.fromString(gameId), creatorName);
    playerRepository.addGameIdToPlayer(UUID.fromString(gameId), joiningPlayerName);

    // link room code to this game ID
    roomRepository.addGameIdToRoomCode(baseRoomCode, UUID.fromString(gameId));

    // delete pending game file since the match has officially started
    gameRepository.deletePendingGame(baseRoomCode, gameId);

    return gameId;
  }

  @Override
  public String createPendingGame(String rawRoomCode, String playerName) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    String gameIdString = UUID.randomUUID().toString();
    gameRepository.createPendingGame(gameIdString, baseRoomCode, playerName);

    return gameIdString;
  }

  @Override
  public boolean deletePendingGame(String rawRoomCode, String gameId) {
    String baseRoomCode = extractBaseRoomCode(rawRoomCode);

    return gameRepository.deletePendingGame(baseRoomCode, gameId);
  }

  /* HELPER FUNCTION: extracts room code from the complete game id and removes 'R's that signifies rematches */
  private String extractBaseRoomCode(String completeGameId) {
    String[] gameInfo = completeGameId.split("_");
    String rawRoomCode = gameInfo[0];

    //On rematch, roomCode gets appended with "R" every time a new match starts
    return rawRoomCode.length() >= 4 ? rawRoomCode.substring(0, 4) : rawRoomCode;
  }

  private GameMove toEntity(MoveRequestDto dto, String pureGameUuid) {
    GameMove move = new GameMove();
    try {
      move.setGameId(UUID.fromString(pureGameUuid));
      move.setPlayerName(dto.getPlayerName());
      move.setSymbol(dto.getSymbol());
      move.setLocation(dto.getLocation());
      move.setDateSave(java.time.LocalDateTime.now());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(ErrorMessage.INVALID_UUID_FORMAT.formatMessage(dto.getGameId()));
    }
    return move;
  }

  private GameMoveDto toDto(GameMove gameMove) {
    GameMoveDto move = new GameMoveDto();
    move.setGameId(gameMove.getGameId());
    move.setPlayerName(gameMove.getPlayerName());
    move.setSymbol(gameMove.getSymbol());
    move.setLocation(gameMove.getLocation());
    move.setDateSaved(gameMove.getDateSave());
    return move;
  }
}
