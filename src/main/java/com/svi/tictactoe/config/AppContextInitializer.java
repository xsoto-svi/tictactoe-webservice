package com.svi.tictactoe.config;

import com.svi.tictactoe.connection.CassandraConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.logging.Logger;

@WebListener
public class AppContextInitializer implements ServletContextListener {

  private static final Logger LOGGER = Logger.getLogger(AppContextInitializer.class.getName());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    LOGGER.info(">>> Tic-Tac-Toe Application is starting up...");
    LOGGER.info(">>> Connecting to Cassandra...");

    try {
      LOGGER.info(">>> Cassandra initialized successfully.");
    } catch (Exception e) {
      LOGGER.severe(">>> Failed to connect to Cassandra: " + e.getMessage());
      throw new RuntimeException("DB Initialization failed", e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    LOGGER.info(">>> Shutting down app, closing DB connection.");
    CassandraConnection.getInstance().close();
  }
}