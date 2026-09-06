package com.svi.tictactoe.service;

import javax.json.JsonObject;
import java.util.List;

public interface RoomService {
    List<JsonObject> getAllRoomCodes();
    List<JsonObject> getGamesByRoomCode(String roomCode);
}

