package io.mpolivakha.streaming_api;

import io.mpolivakha.AbstractIntegrationTest;
import io.mpolivakha.MyEntity;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.support.TransactionTemplate;

@TestPropertySource(properties = {
    "spring.jpa.show-sql=true",
    "spring.jpa.properties.hibernate.format_sql=true"
})
@SpringBootTest(classes = Config.class)
public class StreamApiTest extends AbstractIntegrationTest {

  @Autowired
  private MyEntityJpaRepository myEntityJpaRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @DynamicPropertySource
  static void registerTestContainerProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.username", container::getUsername);
  }

  @Test
  public void findByType() {
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      Stream<MyEntity> stream = myEntityJpaRepository.streamByType(StreamingEntityType.ENHANCED);
      stream.forEach(System.out::println);
    });
  }

  @Test
  public void findByType_withHint() {
    transactionTemplate.executeWithoutResult(transactionStatus -> {
      Stream<MyEntity> stream = myEntityJpaRepository.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              streamWithHintByType(StreamingEntityType.ENHANCED);
      stream.forEach(System.out::println);
    });
  }
}
