package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.request.PendingGameRequestDto;
import com.svi.tictactoe.model.dto.response.*;
import com.svi.tictactoe.service.GameService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("game")
public class GameResource {

  private final GameService gameService;

  @Inject
  public GameResource(GameService gameService) {
    this.gameService = gameService;
  }

  @GET
  @Path("health")
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkHealth() {
    return Response.ok()
            .entity(new ApiResponse("Server is running."))
            .build();
  }

  @POST
  @Path("save")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveMove(@Valid MoveRequestDto moveDto) {
    GameMoveDto savedMove = gameService.saveMove(moveDto);
    return Response.ok()
            .entity(new SaveMoveResponse(savedMove, "Record saved"))
            .build();
  }

  @GET
  @Path("{gameId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGameDetailsByGameId(@PathParam("gameId") UUID gameId) {
    List<GameMoveDto> gameDetailsList = gameService.getGameDetailsByGameId(gameId);
    String message = gameDetailsList.isEmpty() ? "No records found": "Records found.";

    return Response.ok()
            .entity(new GameDetailsResponse(gameDetailsList, message))
            .build();
  }

  @POST
  @Path("create")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createPendingGame(@Valid PendingGameRequestDto pendingGameDto) {
    String gameIdString = gameService.createPendingGame(pendingGameDto.getRoomCode(), pendingGameDto.getPlayerName());

    return Response.ok()
            .entity(new GameIdResponse("Successfully created pending game", gameIdString))
            .build();
  }

  @POST
  @Path("pending")
  @Produces(MediaType.APPLICATION_JSON)
  public Response joinPendingGame(@Valid PendingGameRequestDto pendingGameDto) {
    String pendingGameId = gameService.joinPendingGame(pendingGameDto.getRoomCode(), pendingGameDto.getPlayerName());
    String message = (pendingGameId == null || pendingGameId.isEmpty()) ? "No game found": "Game found.";

    return Response.ok()
            .entity(new GameIdResponse(message, pendingGameId))
            .build();
  }

  @DELETE
  @Path("{roomCode}/pending/{gameId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response cancelPendingGame(@PathParam("roomCode") String rawRoomCode,
          @PathParam("gameId") String gameId) {

    boolean result = gameService.deletePendingGame(rawRoomCode, gameId);
    String message = result ? "Successfully deleted pending game" : "Failed to delete pending game";

    return Response.ok()
            .entity(new ApiResponse(message))
            .build();
  }
}
