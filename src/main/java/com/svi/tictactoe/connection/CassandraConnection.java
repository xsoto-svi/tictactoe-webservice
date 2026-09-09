package com.svi.tictactoe.connection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.svi.tictactoe.config.Config;
import com.svi.tictactoe.exceptions.mapper.CassandraExceptionMapper;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

public class CassandraConnection implements AutoCloseable {

  private final Cluster cluster;
  private final Session session;

  private static final Logger LOGGER = Logger.getLogger(CassandraExceptionMapper.class.getName());

  public CassandraConnection() {
    String host = Config.get(Config.Key.CASSANDRA_IP.value());
    int port = Integer.parseInt(Config.get(Config.Key.CASSANDRA_PORT.value()));

    this.cluster = Cluster.builder()
            .addContactPoint(host)
            .withPort(port)
            .build();

    this.session = cluster.connect(Config.get(Config.Key.CASSANDRA_KEYSPACE.value()));
  }

  private static final class ConnectionHolder {
    private static final CassandraConnection INSTANCE = new CassandraConnection();
  }

  public static CassandraConnection getInstance() {
    return ConnectionHolder.INSTANCE;
  }

  public Session getSession() {
    return session;
  }

  public Cluster getCluster() {
    return cluster;
  }

  @Override
  public void close() {
    if (session != null && !session.isClosed()) {
      session.close();
    }
    if (cluster != null && !cluster.isClosed()) {
      cluster.close();
    }
  }
}