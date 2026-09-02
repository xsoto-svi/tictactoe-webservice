package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.response.ApiResponse;
import com.svi.tictactoe.model.dto.response.GameDetailsResponse;
import com.svi.tictactoe.model.dto.response.GameMoveResponseDto;
import com.svi.tictactoe.model.dto.response.ListGamesResponse;
import com.svi.tictactoe.service.TicTacToeService;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
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
public class TicTacToeResource {

  @Inject
  private TicTacToeService ticTacToeService;

  @GET
  @Path("/health")
  public Response checkHealth() {
    return Response.ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(new ApiResponse("Server is running."))
            .build();
  }

  @POST
  @Path("/save")
  public Response saveMove(@Valid MoveRequestDto moveDto) {
    return Response.ok().build();
  }

  @GET
  @Path("/list-games/{playerId}")
  public Response getGamesByPlayerId(@PathParam("playerId") String playerName) {
    List<UUID> gameUUIDList = ticTacToeService.getGamesByPlayerName(playerName);

    List<ListGamesResponse.GameItemDto> gameItemList = gameUUIDList.stream()
            .map(ListGamesResponse.GameItemDto::new)
            .collect(Collectors.toList());

    if (gameItemList.isEmpty()) {
      return Response.ok()
              .type(MediaType.APPLICATION_JSON)
              .entity(new ListGamesResponse(gameItemList, "No records found."))
              .build();
    }

    return Response.ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(new ListGamesResponse(gameItemList, "Records found."))
            .build();
  }

  @GET
  @Path("/game/{gameId}")
  public Response getGameDetailsByGameId(@PathParam("gameId") UUID gameId) {
    List<GameMoveResponseDto> gameDetailsList = ticTacToeService.getGameDetailsByGameId(gameId);

    if (gameDetailsList.isEmpty()) {
      return Response.ok()
              .type(MediaType.APPLICATION_JSON)
              .entity(new GameDetailsResponse(gameDetailsList, "No records found"))
              .build();
    }

    return Response.ok()
            .type(MediaType.APPLICATION_JSON)
            .entity(new GameDetailsResponse(gameDetailsList, "Records found."))
            .build();
  }



}
