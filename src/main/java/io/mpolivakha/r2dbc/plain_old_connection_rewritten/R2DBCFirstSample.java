package io.mpolivakha.r2dbc.plain_old_connection_rewritten;

import io.mpolivakha.MyEntity;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class R2DBCFirstSample {

  private final String connectionString;
  private final String username;
  private final String password;

  public AtomicInteger receivedCounter = new AtomicInteger(0);

  public R2DBCFirstSample(String connectionString, String username, String password) {
    this.connectionString = connectionString;
    this.username = username;
    this.password = password;
  }

  public void findMyAllEntities() {
    ConnectionFactoryOptions build = ConnectionFactoryOptions
        .builder()
        .from(ConnectionFactoryOptions.parse(connectionString))
        .option(ConnectionFactoryOptions.USER, username)
        .option(ConnectionFactoryOptions.PASSWORD, password)
        .build();

    ConnectionFactory connectionFactory = ConnectionFactories.get(build);

    connectionFactory
        .create()
        .subscribe(new BaseSubscriber<Connection>() {

          @Override
          public void hookOnNext(Connection connection) {
            Statement statement = connection.createStatement("select * from my_entity");

            Flux.from(statement.execute())
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(new BaseSubscriber<Result>() {

                  @Override
                  public void hookOnNext(Result result) {
                    result
                        .map((row, rowMetadata) -> new MyEntity(row.get("id", Long.class)))
                        .subscribe(new BaseSubscriber<>() {
                          @Override
                          public void hookOnNext(MyEntity myEntity) {
                            receivedCounter.incrementAndGet();
                          }
                        });
                  }

                  @Override
                  public void hookOnComplete() {
                    connection.close();
                  }
                });
          }
        });
  }
}
