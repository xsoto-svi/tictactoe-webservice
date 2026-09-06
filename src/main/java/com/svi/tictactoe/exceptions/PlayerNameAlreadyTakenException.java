package com.svi.tictactoe.exceptions;

import javax.ws.rs.core.Response;

public class PlayerNameAlreadyTakenException extends ApiException {
  public PlayerNameAlreadyTakenException(String message) { super(Response.Status.BAD_REQUEST, message); }
}
