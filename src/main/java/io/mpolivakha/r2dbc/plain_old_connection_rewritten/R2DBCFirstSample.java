package io.mpolivakha.r2dbc.plain_old_connection_rewritten;

import io.mpolivakha.MyEntity;
import io.mpolivakha.util.ImmediateSubscriber;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import java.util.concurrent.atomic.AtomicInteger;

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
        .subscribe(new ImmediateSubscriber<Connection>() {

          @Override
          public void onNext(Connection connection) {
            Statement statement = connection.createStatement("select * from my_entity");

            statement
                .execute()
                .subscribe(new ImmediateSubscriber<Result>() {

                  @Override
                  public void onNext(Result result) {
                    result
                        .map((row, rowMetadata) -> new MyEntity(row.get("id", Long.class)))
                        .subscribe(new ImmediateSubscriber<>() {
                          @Override
                          public void onNext(MyEntity myEntity) {
                            receivedCounter.incrementAndGet();
                          }
                        });
                  }

                  @Override
                  public void onComplete() {
                    connection.close();
                  }
                });
          }
        });
  }
}
