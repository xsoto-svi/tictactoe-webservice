package com.svi.tictactoe.resource;

import com.svi.tictactoe.model.dto.response.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.svi.tictactoe.constants.SuccessMessage;

@Path("health")
public class HealthController {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkHealth() {
    return Response.ok()
            .entity(new ApiResponse(SuccessMessage.SERVER_RUNNING.getMessage()))
            .build();
  }
}