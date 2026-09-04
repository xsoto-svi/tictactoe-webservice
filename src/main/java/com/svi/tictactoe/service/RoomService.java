package com.svi.tictactoe.service;
import com.svi.tictactoe.repository.FileGameRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoomService {

  private FileGameRepository fileGameRepository;

  public RoomService() {}

  @Inject
  public RoomService(FileGameRepository fileGameRepository) {
    this.fileGameRepository = fileGameRepository;
  }

  public List<JsonObject> getAllRoomCodes() {
    return fileGameRepository.getAllRoomCodes().stream()
            .map(code -> Json.createObjectBuilder()
                    .add("roomcode", code)
                    .build())
            .collect(Collectors.toList());
  }

  public List<JsonObject> getGamesByRoomCode(String roomCode) {
    return fileGameRepository.getAllGamesByRoomCode(roomCode).stream()
            .map(id -> Json.createObjectBuilder()
                    .add("id", id.toString())
                    .build())
            .collect(Collectors.toList());
  }


}
