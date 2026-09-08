package com.svi.tictactoe.constants;

public enum SuccessMessage {
  RECORDS_FOUND("Records found."),
  SERVER_RUNNING("Server is running."),
  RECORD_SAVED("Record saved"),
  GAME_CREATED("Successfully created pending game"),
  GAME_FOUND("Game found."),
  GAME_DELETED("Successfully deleted pending game");

  private final String message;

  SuccessMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public String formatMessage(Object... args) {
    return String.format(message, args);
  }
}

