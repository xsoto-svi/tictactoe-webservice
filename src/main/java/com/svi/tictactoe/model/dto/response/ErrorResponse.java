package com.svi.tictactoe.model.dto.response;

import java.util.List;

public class ErrorResponse extends ApiResponse {
  private final List<String> errors;

  public ErrorResponse(String message, List<String> errors) {
    super(message);
    this.errors = errors;
  }

  public List<String> getErrors() {
    return errors;
  }
}
