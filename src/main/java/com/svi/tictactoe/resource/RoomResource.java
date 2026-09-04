package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.response.ListJsonObjectResponse;
import com.svi.tictactoe.service.RoomService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("room")
public class RoomResource {

  private final RoomService roomService;

  @Inject
  public RoomResource(RoomService roomService) {
    this.roomService = roomService;
  }

  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllRoomCodes() {
    List<JsonObject> roomCodeJsonObjects = roomService.getAllRoomCodes();
    String message = roomCodeJsonObjects.isEmpty() ? "No records found" : "Records found";

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, roomCodeJsonObjects))
            .build();
  }

  @GET
  @Path("{roomCode}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGamesByRoomCode(@PathParam("roomCode") String roomCode) {
    List<JsonObject> gameUuidJsonObjects = roomService.getGamesByRoomCode(roomCode);
    String message = gameUuidJsonObjects.isEmpty() ? "No records found" : "Records found";

    return Response.ok()
            .entity(new ListJsonObjectResponse(message, gameUuidJsonObjects))
            .build();
  }
}
