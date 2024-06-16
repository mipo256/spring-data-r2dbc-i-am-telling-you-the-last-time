package io.mpolivakha;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class AbstractIntegrationTest {

  protected final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.2"));

  @BeforeAll
  static void beforeAll() {
    container.withInitScript("init.sql");
    container.start();
  }

  @AfterAll
  static void afterAll() {
    container.stop();
  }
}
