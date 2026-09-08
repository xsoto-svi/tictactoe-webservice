package com.svi.tictactoe.constants;

import javax.ws.rs.core.Response.Status;

public enum ErrorMessage {
  ROOM_NOT_FOUND("The requested room code does not exist."),
  NAME_TAKEN("Player name '%s' is already taken in this room."),
  INVALID_PAYLOAD("The request payload is missing required fields."),
  INTERNAL_SERVER_ERROR("An unexpected error occurred while processing the file."),
  NO_RECORDS_FOUND("No records found."),
  NO_GAME_FOUND("No game found"),
  DELETE_GAME_FAILED("Failed to delete pending game"),
  LOCATION_OCCUPIED("Location %s is already occupied"),
  INVALID_UUID_FORMAT("Invalid UUID format for gameId: %s");

  private final String message;

  ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }
}
