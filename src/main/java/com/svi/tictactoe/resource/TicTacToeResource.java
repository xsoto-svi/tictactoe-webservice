package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.request.MoveRequestDto;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TicTacToeResource {

  @POST
  @Path("/save")
  public Response saveMove(@Valid MoveRequestDto moveDto) {
    return Response.ok().build();
  }



}
