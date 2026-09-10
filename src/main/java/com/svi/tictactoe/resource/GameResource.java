package com.svi.tictactoe.resource;

import com.svi.tictactoe.constants.ErrorMessage;
import com.svi.tictactoe.constants.SuccessMessage;
import com.svi.tictactoe.model.dto.request.MoveRequestDto;
import com.svi.tictactoe.model.dto.request.PendingGameRequestDto;
import com.svi.tictactoe.model.dto.response.*;
import com.svi.tictactoe.service.GameService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("games")
public class GameResource {

  private final GameService gameService;

  @Inject
  public GameResource(GameService gameService) {
    this.gameService = gameService;
  }

  @POST
  @Path("save")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveMove(@Valid MoveRequestDto moveDto) {
    GameMoveDto savedMove = gameService.saveMove(moveDto);
    return Response.ok()
            .entity(new SaveMoveResponse(savedMove, SuccessMessage.RECORD_SAVED.getMessage()))
            .build();
  }

  @GET
  @Path("{gameId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGameDetailsByGameId(@PathParam("gameId") UUID gameId) {
    List<GameMoveDto> gameDetailsList = gameService.getGameDetailsByGameId(gameId);
    String message = gameDetailsList.isEmpty() ? ErrorMessage.NO_RECORDS_FOUND.getMessage() : SuccessMessage.RECORDS_FOUND.getMessage();

    return Response.ok()
            .entity(new GameDetailsResponse(gameDetailsList, message))
            .build();
  }

  @POST
  @Path("create-pending")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createPendingGame(@Valid PendingGameRequestDto pendingGameDto) {
    String gameIdString = gameService.createPendingGame(pendingGameDto.getRoomCode(), pendingGameDto.getPlayerName());

    return Response.ok()
            .entity(new GameIdResponse(SuccessMessage.GAME_CREATED.getMessage(), gameIdString))
            .build();
  }

  @POST
  @Path("pending")
  @Produces(MediaType.APPLICATION_JSON)
  public Response joinPendingGame(@Valid PendingGameRequestDto pendingGameDto) {
    String pendingGameId = gameService.joinPendingGame(pendingGameDto.getRoomCode(), pendingGameDto.getPlayerName());
    String message = (pendingGameId == null || pendingGameId.isEmpty()) ? ErrorMessage.NO_GAME_FOUND.getMessage() : SuccessMessage.GAME_FOUND.getMessage();

    return Response.ok()
            .entity(new GameIdResponse(message, pendingGameId))
            .build();
  }

  @DELETE
  @Path("{roomCode}/pending/{gameId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response cancelPendingGame(@PathParam("roomCode") String rawRoomCode,
          @PathParam("gameId") String gameId) {

    boolean result = gameService.cancelPendingGame(rawRoomCode, gameId);
    String message = result ? SuccessMessage.GAME_DELETED.getMessage() : ErrorMessage.DELETE_GAME_FAILED.getMessage();

    return Response.ok()
            .entity(new ApiResponse(message))
            .build();
  }
}
