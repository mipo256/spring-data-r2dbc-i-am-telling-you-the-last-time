package io.mpolivakha.r2dbc.web_client_example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.mpolivakha.util.ImmediateSubscriber;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.SneakyThrows;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

public class R2DBCodeSample {

  private final WebClient webClient;

  private final AgreementRepository agreementRepository;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  public R2DBCodeSample(String baseUrl, AgreementRepository agreementRepository) {
    this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    this.agreementRepository = agreementRepository;
  }

  public void findAgreement(@NonNull String id, String type) {
    Assert.notNull(id, "Id cannot be null at this point");

    AgreementType agreementType = Optional.ofNullable(type).filter(it -> !it.isEmpty()).map(AgreementType::valueOf).orElse(AgreementType.STANDARD);

    webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/v1/agreements/{type}/{id}").build(agreementType, id))
        .header(
            HttpHeaders.AUTHORIZATION,
            "Basic ".concat(Base64.getEncoder().encodeToString("123:hello".getBytes(StandardCharsets.UTF_8)))
        )
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(String.class)
        .subscribeOn(Schedulers.immediate())
        .subscribe(new ImmediateSubscriber<>() {
          @SneakyThrows
          @Override
          public void onNext(String body) {
            AgreementResponse agreementResponse = objectMapper.readValue(body, AgreementResponse.class);
            if (agreementResponse.getExpiresAt().isAfter(OffsetDateTime.now())) {
              agreementRepository.renewAgreement(agreementResponse);
            }
          }
        });

    doOtherStuff();
  }

  private void doOtherStuff() {
    System.out.printf("'%s' is doing other stuff%n", Thread.currentThread().getName());
  }
}
