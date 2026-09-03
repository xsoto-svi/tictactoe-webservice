package com.svi.tictactoe.exceptions.mapper;

import com.svi.tictactoe.exceptions.ApiException;
import com.svi.tictactoe.model.dto.response.ApiResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<ApiException> {

  @Override
  public Response toResponse(ApiException exception) {
    return Response.status(exception.getStatus())
            .type(MediaType.APPLICATION_JSON)
            .entity(new ApiResponse(exception.getMessage()))
            .build();
  }
}
