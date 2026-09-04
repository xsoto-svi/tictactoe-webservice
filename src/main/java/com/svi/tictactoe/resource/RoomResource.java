package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.response.ListJsonObjectResponse;
import com.svi.tictactoe.service.GameService;
import com.svi.tictactoe.service.PlayerService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("room")
public class RoomResource {

  private final GameService gameService;

  @Inject
  public RoomResource(GameService gameService) {
    this.gameService = gameService;
  }

  @GET
  @Path("{roomCode}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGamesByRoomCode(@PathParam("roomCode") String roomCode) {
    List<JsonObject> gameUuidJsonObjects = gameService.getGamesByRoomCode(roomCode);
    String message = gameUuidJsonObjects.isEmpty() ? "No records found" : "Records found";

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, gameUuidJsonObjects))
            .build();
  }
}
