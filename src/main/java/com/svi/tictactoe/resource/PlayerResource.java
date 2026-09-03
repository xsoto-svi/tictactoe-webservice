package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.response.ListGamesResponse;
import com.svi.tictactoe.service.GameService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("player")
public class PlayerResource {

  private final GameService gameService;

  @Inject
  public PlayerResource(GameService gameService) {
    this.gameService = gameService;
  }

  @GET
  @Path("{playerName}/games")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response getGamesByPlayerId(@PathParam("playerName") String playerName) {
    List<UUID> gameUUIDList = gameService.getGamesByPlayerName(playerName);

    List<ListGamesResponse.GameItemDto> gameItemList = gameUUIDList.stream()
            .map(ListGamesResponse.GameItemDto::new)
            .collect(Collectors.toList());

    if (gameItemList.isEmpty()) {
      return Response.ok()
              .entity(new ListGamesResponse(gameItemList, "No records found."))
              .build();
    }

    return Response.ok()
            .entity(new ListGamesResponse(gameItemList, "Records found."))
            .build();
  }
}
