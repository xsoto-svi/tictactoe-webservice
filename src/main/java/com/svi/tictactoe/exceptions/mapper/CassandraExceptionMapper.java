package com.svi.tictactoe.exceptions.mapper;

import com.datastax.oss.driver.api.core.DriverException;
import com.svi.tictactoe.constants.ErrorMessage;
import com.svi.tictactoe.model.dto.response.ApiResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class CassandraExceptionMapper implements ExceptionMapper<DriverException> {

  private static final Logger LOGGER = Logger.getLogger(CassandraExceptionMapper.class.getName());

  @Override
  public Response toResponse(DriverException exception) {
    LOGGER.info("Cassandra DB Error: " + exception.getMessage());

    return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new ApiResponse(ErrorMessage.INTERNAL_SERVER_ERROR.getMessage()))
            .type(MediaType.APPLICATION_JSON)
            .build();
  }
}