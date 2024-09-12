package io.mpolivakha.jdbc;

import io.mpolivakha.MyEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.postgresql.ds.PGSimpleDataSource;

public class PlainOldConnection {

  private final String username;
  private final String password;
  private final String jdbcUrl;

  public PlainOldConnection(String username, String password, String jdbcUrl) {
    this.username = username;
    this.password = password;
    this.jdbcUrl = jdbcUrl;
  }

  public Optional<MyEntity> findById(Long id) {
    var pgSimpleDataSource = new PGSimpleDataSource();
    pgSimpleDataSource.setUser(username);
    pgSimpleDataSource.setPassword(password);
    pgSimpleDataSource.setUrl(jdbcUrl);

    try (java.sql.Connection connection = pgSimpleDataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM my_entity WHERE id = ?")) {

      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      if (resultSet.next()) {
        return Optional.of(new MyEntity(resultSet.getLong("id")));
      }
      return Optional.empty();
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public CompletableFuture<Optional<MyEntity>> findByIdAsync(Long id) {
    var pgSimpleDataSource = new PGSimpleDataSource();
    pgSimpleDataSource.setUser(username);
    pgSimpleDataSource.setPassword(password);
    pgSimpleDataSource.setUrl(jdbcUrl);

    return CompletableFuture
        .supplyAsync(() -> {
          try {
            return pgSimpleDataSource.getConnection();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        })
        .thenApplyAsync(connection -> {
          try {
            return connection.prepareStatement("SELECT * FROM my_entity WHERE id = ?");
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }).thenApplyAsync(preparedStatement -> {
          try {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
              return Optional.of(new MyEntity(resultSet.getLong("id")));
            }
            return Optional.empty();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        });
  }
}
