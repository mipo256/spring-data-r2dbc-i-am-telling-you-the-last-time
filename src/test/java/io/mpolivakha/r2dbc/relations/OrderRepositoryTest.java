package io.mpolivakha.r2dbc.relations;

import io.mpolivakha.AbstractPostgreSQLIntegrationTest;
import io.mpolivakha.r2dbc.relations.OrderRepositoryTest.CurrentConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CurrentConfiguration.class)
class OrderRepositoryTest extends AbstractPostgreSQLIntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ConnectionFactory connectionFactory;

  @Configuration
  static class CurrentConfiguration {

    @Bean
    R2dbcRepositoryFactory r2dbcRepositoryFactory(DatabaseClient databaseClient) {
      return new R2dbcRepositoryFactory(
          databaseClient,
          new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE, List.of())
      );
    }

    @Bean
    OrderRepository orderRepository(R2dbcRepositoryFactory r2dbcRepositoryFactory) {
      return r2dbcRepositoryFactory.getRepository(OrderRepository.class);
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
      return DatabaseClient.builder().connectionFactory(connectionFactory).build();
    }

    @Bean
    ConnectionFactory connectionFactory(
        @Value("${spring.r2dbc.url}") String r2dbcUrl,
        @Value("${spring.r2dbc.username}") String username,
        @Value("${spring.r2dbc.password}") String password
        ) {
      ConnectionFactoryOptions build = ConnectionFactoryOptions
          .builder()
          .from(ConnectionFactoryOptions.parse(r2dbcUrl))
          .option(ConnectionFactoryOptions.USER, username)
          .option(ConnectionFactoryOptions.PASSWORD, password)
          .build();

      return ConnectionFactories.get(build);
    }
  }

  @DynamicPropertySource
  static void register(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://%s:%d/%s".formatted(container.getHost(), container.getFirstMappedPort(), container.getDatabaseName()));
    registry.add("spring.r2dbc.username", container::getUsername);
    registry.add("spring.r2dbc.password", container::getPassword);
  }

  @BeforeEach
  void setUp() {
    Mono
        .fromDirect(connectionFactory.create())
        .subscribe(connection ->
            Flux.from(
                connection
                    .createStatement(
                        //language=sql
                        """
                            CREATE TABLE IF NOT EXISTS orders(
                              id BIGSERIAL PRIMARY KEY,
                              status TEXT
                            );
                            CREATE TABLE IF NOT EXISTS order_item(
                              id BIGSERIAL PRIMARY KEY,
                              name TEXT,
                              order_id BIGINT REFERENCES orders(id)
                            );
                            
                            INSERT INTO orders(id, "status") VALUES(1, 'NEW');
                            INSERT INTO order_item("name", order_id) VALUES('Lego', 1);
                        """)
                    .execute()
            ).subscribe()
        );
  }

  @Test
  void testR2dbcAggregateLoading() {
    StepVerifier
        .create(orderRepository.findById(1L))
        .expectNextMatches(order -> {
          System.out.println(order);
          return order.getOrderItems().size() == 2 && order.getStatus().equals("NEW");
        })
        .expectComplete()
        .verify();
  }
}