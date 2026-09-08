package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.response.ListJsonObjectResponse;
import com.svi.tictactoe.service.RoomService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import com.svi.tictactoe.constants.ErrorMessage;
import com.svi.tictactoe.constants.SuccessMessage;

@Path("rooms")
public class RoomResource {

  private final RoomService roomService;

  @Inject
  public RoomResource(RoomService roomService) {
    this.roomService = roomService;
  }

  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllRoomCodes() {
    List<JsonObject> roomCodeJsonObjects = roomService.getAllRoomCodes();
    String message = roomCodeJsonObjects.isEmpty() ? ErrorMessage.NO_RECORDS_FOUND.getMessage() : SuccessMessage.RECORDS_FOUND.getMessage();

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, roomCodeJsonObjects))
            .build();
  }

  @GET
  @Path("{roomCode}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGamesByRoomCode(@PathParam("roomCode") String roomCode) {
    List<JsonObject> gameUuidJsonObjects = roomService.getGamesByRoomCode(roomCode);
    String message = gameUuidJsonObjects.isEmpty() ? ErrorMessage.NO_RECORDS_FOUND.getMessage() : SuccessMessage.RECORDS_FOUND.getMessage();

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, gameUuidJsonObjects))
            .build();
  }
}
