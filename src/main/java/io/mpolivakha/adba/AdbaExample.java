package io.mpolivakha.adba;

import io.mpolivakha.MyEntity;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collector;
import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.DataSourceFactory;
import jdk.incubator.sql2.Session;
import jdk.incubator.sql2.TransactionCompletion;
import lombok.SneakyThrows;
import org.postgresql.adba.PgDataSourceFactory;

public class AdbaExample {

  private final String jdbcUrl;
  private final String username;
  private final String password;

  public AdbaExample(String jdbcUrl, String username, String password) {
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
  }

  @SneakyThrows
  public MyEntity findMyAllEntities() {
    DataSourceFactory factory = new PgDataSourceFactory();
    try (DataSource ds = factory.builder()
        .url(jdbcUrl)
        .username(username)
        .password(password)
        .build();
        Session conn = ds.getSession(t -> System.out.println("ERROR: " + t.getMessage()))) {
      TransactionCompletion trans = conn.transactionCompletion();

      MyEntity myEntity = conn.<MyEntity>rowOperation("select * from my_entity")
          .collect(Collector.of(
              () -> new MyEntity[1],
              (array, resultSet) -> array[0] = new MyEntity(resultSet.at(1).get(Long.class)),
              (first, second) -> first,
              array -> array[0])
          )
          .submit() // Submission<MyEntity>
          .getCompletionStage()
          .toCompletableFuture()
          .get();

      conn.catchErrors().onError(Throwable::printStackTrace).commitMaybeRollback(trans);

      return myEntity;
    }
  }
}
