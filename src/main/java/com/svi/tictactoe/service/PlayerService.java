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
public class PlayerService {

  private FileGameRepository fileGameRepository;

  public PlayerService() {}

  @Inject
  public PlayerService(FileGameRepository fileGameRepository) {
    this.fileGameRepository = fileGameRepository;
  }

  public List<JsonObject> getAllPlayerNames() {
    return fileGameRepository.getAllPlayerNames().stream()
            .map(name -> Json.createObjectBuilder()
                    .add("playername", name)
                    .build())
            .collect(Collectors.toList());
  }

  public List<JsonObject> getGamesByPlayerName(String name) {
    return fileGameRepository.getGamesByPlayerName(name).stream()
            .map(gameUuid -> Json.createObjectBuilder()
                    .add("id", gameUuid.toString())
                    .build())
            .collect(Collectors.toList()
    );
  }
}
