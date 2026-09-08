package com.svi.tictactoe.exceptions;

import com.svi.tictactoe.constants.ErrorMessage;

import javax.ws.rs.core.Response;

public class PlayerNameAlreadyTakenException extends ApiException {
  public PlayerNameAlreadyTakenException(String playerName) {
    super(Response.Status.CONFLICT, ErrorMessage.NAME_TAKEN.formatMessage(playerName));
  }
}
