package com.svi.tictactoe.resource;

import com.svi.tictactoe.constants.ErrorMessage;
import com.svi.tictactoe.constants.SuccessMessage;
import com.svi.tictactoe.model.dto.response.ListJsonObjectResponse;
import com.svi.tictactoe.service.PlayerService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("players")
public class PlayerResource {

  private final PlayerService playerService;

  @Inject
  public PlayerResource(PlayerService playerService) {
    this.playerService = playerService;
  }

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllPlayerNames() {
    List<JsonObject> playerNameJsonObjects = playerService.getAllPlayerNames();
    String message = playerNameJsonObjects.isEmpty() ? ErrorMessage.NO_RECORDS_FOUND.getMessage() : SuccessMessage.RECORDS_FOUND.getMessage();

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, playerNameJsonObjects))
            .build();
  }

  @GET
  @Path("{playerName}/games")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGamesByPlayerName(@PathParam("playerName") String playerName) {
    List<JsonObject> gameUuidJsonObjects = playerService.getGamesByPlayerName(playerName);
    String message = gameUuidJsonObjects.isEmpty() ? ErrorMessage.NO_RECORDS_FOUND.getMessage() : SuccessMessage.RECORDS_FOUND.getMessage();

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, gameUuidJsonObjects))
            .build();
  }
}
