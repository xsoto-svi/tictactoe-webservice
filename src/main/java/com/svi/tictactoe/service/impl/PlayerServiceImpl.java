package com.svi.tictactoe.service.impl;
import com.svi.tictactoe.repository.PlayerRepository;
import com.svi.tictactoe.service.PlayerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlayerServiceImpl implements PlayerService {

  private PlayerRepository playerRepository;

  public PlayerServiceImpl() {}

  @Inject
  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public List<JsonObject> getAllPlayerNames() {
    return playerRepository.getPlayerNames().stream()
            .map(name -> Json.createObjectBuilder()
                    .add("playername", name)
                    .build())
            .collect(Collectors.toList());
  }

  @Override
  public List<JsonObject> getGamesByPlayerName(String name) {
    return playerRepository.getGamesByPlayerName(name).stream()
            .map(gameUuid -> Json.createObjectBuilder()
                    .add("id", gameUuid.toString())
                    .build())
            .collect(Collectors.toList()
    );
  }
}
