package com.svi.tictactoe.exceptions;

import javax.ws.rs.core.Response;

public class InvalidMoveException extends ApiException {
  public InvalidMoveException(String message) {
    super(Response.Status.BAD_REQUEST, message);
  }
}
