package com.svi.tictactoe;

import com.svi.tictactoe.model.dto.response.ApiResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationPath("api")
public class TicTacToeApplication extends Application {

  @GET
  @Path("health")
  @Produces(MediaType.APPLICATION_JSON)
  public Response checkHealth() {
    return Response.ok()
            .entity(new ApiResponse("Server is running."))
            .build();
  }
}
