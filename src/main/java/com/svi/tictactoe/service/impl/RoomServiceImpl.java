package com.svi.tictactoe.service.impl;
import com.svi.tictactoe.repository.RoomRepository;
import com.svi.tictactoe.service.RoomService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoomServiceImpl implements RoomService {

  private RoomRepository roomRepository;

  public RoomServiceImpl() {}

  @Inject
  public RoomServiceImpl(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Override
  public List<JsonObject> getAllRoomCodes() {
    return roomRepository.getRoomCodes().stream()
            .map(code -> Json.createObjectBuilder()
                    .add("roomcode", code)
                    .build())
            .collect(Collectors.toList());
  }

  @Override
  public List<JsonObject> getGamesByRoomCode(String roomCode) {
    return roomRepository.getGamesByRoomCode(roomCode).stream()
            .map(id -> Json.createObjectBuilder()
                    .add("id", id.toString())
                    .build())
            .collect(Collectors.toList());
  }
}
