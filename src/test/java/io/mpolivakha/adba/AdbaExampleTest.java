package io.mpolivakha.adba;

import static org.assertj.core.api.Assertions.assertThat;

import io.mpolivakha.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

class AdbaExampleTest extends AbstractIntegrationTest {

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