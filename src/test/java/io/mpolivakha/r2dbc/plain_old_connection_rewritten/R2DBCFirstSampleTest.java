package io.mpolivakha.r2dbc.plain_old_connection_rewritten;

import io.mpolivakha.AbstractPostgreSQLIntegrationTest;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.awaitility.Awaitility;

class R2DBCFirstSampleTest extends AbstractPostgreSQLIntegrationTest {

  @Test
  void findMyAllEntities() {
    R2DBCFirstSample r2DBCFirstSample = new R2DBCFirstSample(
        "r2dbc:postgresql://%s:%d/%s".formatted(container.getHost(), container.getFirstMappedPort(), container.getDatabaseName()),
        container.getUsername(),
        container.getPassword()
    );

    r2DBCFirstSample.findMyAllEntities();

    Awaitility.await().atMost(Duration.ofSeconds(10)).until(() -> r2DBCFirstSample.receivedCounter.get() == 1);
  }
}