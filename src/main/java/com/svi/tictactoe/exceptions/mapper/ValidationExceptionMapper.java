package com.svi.tictactoe.exceptions.mapper;

import com.svi.tictactoe.model.dto.response.ErrorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException exception) {

    Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

    List<String> errors = violations.stream()
            .map(violation -> violation.getPropertyPath()
                    .toString()
                    .substring(violation.getPropertyPath().toString().lastIndexOf('.') + 1) + ": " + violation.getMessage())
            .collect(Collectors.toList());

    return Response.status(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(new ErrorResponse("Validation failed.", errors))
            .build();
  }
}
