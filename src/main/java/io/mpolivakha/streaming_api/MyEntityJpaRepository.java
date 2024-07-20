package io.mpolivakha.streaming_api;

import io.mpolivakha.MyEntity;
import jakarta.persistence.QueryHint;
import java.util.stream.Stream;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface MyEntityJpaRepository extends JpaRepository<MyStreamingEntity, Long> {

  Stream<MyEntity> streamByType(StreamingEntityType type);

  @QueryHints(
      @QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "10")
  )
  Stream<MyEntity> streamWithHintByType(StreamingEntityType type);
}
