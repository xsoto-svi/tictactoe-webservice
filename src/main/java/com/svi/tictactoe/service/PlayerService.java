package com.svi.tictactoe.service;

import javax.json.JsonObject;
import java.util.List;

public interface PlayerService {
    List<JsonObject> getAllPlayerNames();
    List<JsonObject> getGamesByPlayerName(String name);
}

