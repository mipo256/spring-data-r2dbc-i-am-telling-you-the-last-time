package io.mpolivakha;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;

class PlainOldConnectionTest extends AbstractIntegrationTest {

  @Test
  void findById() {

    // Given.
    PlainOldConnection plainOldConnection = new PlainOldConnection(
        container.getUsername(),
        container.getPassword(),
        container.getJdbcUrl()
    );

    // When.
    Optional<MyEntity> result = plainOldConnection.findById(1L);

    // Then.
    Assertions.assertThat(result).isPresent();
  }

  @Test
  void findByIdAsync() {

    // Given.
    PlainOldConnection plainOldConnection = new PlainOldConnection(
        container.getUsername(),
        container.getPassword(),
        container.getJdbcUrl()
    );

    // When.
    CompletableFuture<Optional<MyEntity>> result = plainOldConnection.findByIdAsync(1L);

    // Then.
    ObjectAssert<Optional<MyEntity>> optionalObjectAssert = Assertions.assertThat(result).succeedsWithin(Duration.ofSeconds(10L));
    optionalObjectAssert.matches(Optional::isPresent);
  }
}