package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.*;
import com.svi.tictactoe.service.GameService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameResource {

  @Inject
  private GameService gameService;

  @GET
  @Path("health")
  public Response checkHealth() {
    return Response.ok()
            .entity(new ApiResponse("Server is running."))
            .build();
  }

  @POST
  @Path("game/save")
  public Response saveMove(@Valid MoveRequestDto moveDto) {
    GameMoveDto savedMove = gameService.saveMove(moveDto);
    return Response.ok()
            .entity(new SaveMoveResponse(savedMove, "Record saved"))
            .build();
  }

  @GET
  @Path("player/{playerName}/games")
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

  @GET
  @Path("game/{gameId}")
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
