package com.svi.tictactoe.model.dto.response;

import javax.json.JsonObject;
import java.util.List;

public class ListJsonObjectResponse extends ApiResponse {
  private List<JsonObject> list;

  public ListJsonObjectResponse(String message, List<JsonObject> list) {
    super(message);
    this.list = list;
  }

  public List<JsonObject> getList() {
    return list;
  }

  public void setList(List<JsonObject> list) {
    this.list = list;
  }
}
