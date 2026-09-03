package com.svi.tictactoe.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TicTacToeIntegrationTest {

  private static final String BASE_URL = "http://localhost:8080";

//  @BeforeAll
//  public static void setup() {
//    RestAssured.baseURI = BASE_URL;
//    RestAssured.basePath = "/api";
//  }
//
//  /**
//   * Automatically cleans up any files created in the data/ directory
//   * after each test finishes, keeping your local environment pristine.
//   */
//  @AfterEach
//  public void cleanupTestData() throws IOException {
//    Path dataDir = Paths.get("data");
//    if (Files.exists(dataDir)) {
//      // Walk through the data directory and delete all contents inside games/ and players/
//      Files.walk(dataDir)
//              .sorted(Comparator.reverseOrder())
//              .filter(path -> !path.equals(dataDir)) // Keep the root "data" folder itself
//              .forEach(path -> {
//                try {
//                  Files.delete(path);
//                } catch (IOException e) {
//                  // Ignore if files are already gone or locked
//                }
//              });
//    }
//  }
//
//  @Test
//  public void testHealthEndpoint() {
//    given()
//            .when()
//            .get("/health")
//            .then()
//            .statusCode(200)
//            .body("message", equalTo("Server is running."));
//  }
//
//  @Test
//  public void testGetGamesForNonExistentPlayer() {
//    String testPlayer = "TestUser_" + System.currentTimeMillis();
//
//    given()
//            .pathParam("playerId", testPlayer)
//            .when()
//            .get("/list-games/{playerId}")
//            .then()
//            .statusCode(200)
//            .body("msg", equalTo("No records found."))
//            .body("list", empty());
//  }
//
//  @Test
//  public void testGetGameDetailsForNonExistentGame() {
//    String randomGameId = "00000000-0000-0000-0000-000000000000";
//
//    given()
//            .pathParam("gameId", randomGameId)
//            .when()
//            .get("/game/{gameId}")
//            .then()
//            .statusCode(200)
//            .body("msg", equalTo("No records found"))
//            .body("list", empty());
//  }
}