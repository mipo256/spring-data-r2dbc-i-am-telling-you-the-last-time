package io.mpolivakha.r2dbc.web_client_example;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class AgreementResponse {

  @JsonProperty("type")
  private AgreementType type;

  @JsonProperty("id")
  private String id;

  @JsonProperty("expiresAt")
  private OffsetDateTime expiresAt;
}
