package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.*;
import com.svi.tictactoe.service.GameService;

import javax.inject.Inject;
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

  @GET
  @Path("generate")
  @Produces(MediaType.APPLICATION_JSON)
  public Response generateGameId() {
    return Response.ok()
            .entity(new GameIdResponse("Successfully generated game id", gameService.generateGameId()))
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

    if (gameDetailsList.isEmpty()) {
      return Response.ok()
              .entity(new GameDetailsResponse(gameDetailsList, "No records found"))
              .build();
    }

    return Response.ok()
            .entity(new GameDetailsResponse(gameDetailsList, "Records found."))
            .build();
  }
}
