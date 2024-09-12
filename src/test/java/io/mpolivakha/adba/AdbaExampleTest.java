package io.mpolivakha.adba;

import static org.assertj.core.api.Assertions.assertThat;

import io.mpolivakha.AbstractPostgreSQLIntegrationTest;
import org.junit.jupiter.api.Test;

class AdbaExampleTest extends AbstractPostgreSQLIntegrationTest {

  @Test
  void findMyAllEntities() {
    AdbaExample adbaExample = new AdbaExample(
        "jdbc:postgresql://%s:%d/%s".formatted(container.getHost(), container.getFirstMappedPort(), container.getDatabaseName()),
        container.getUsername(),
        container.getPassword()
    );

    assertThat(adbaExample.findMyAllEntities()).isNotNull();
  }
}