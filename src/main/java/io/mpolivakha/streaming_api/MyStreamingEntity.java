package io.mpolivakha.streaming_api;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MyStreamingEntity {

  @Id
  private Long id;

  @Enumerated(EnumType.STRING)
  private StreamingEntityType type;
}
