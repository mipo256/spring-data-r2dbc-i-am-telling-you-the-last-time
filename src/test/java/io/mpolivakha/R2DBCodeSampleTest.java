package io.mpolivakha;

import io.mpolivakha.r2dbc.web_client_example.AgreementRepository;
import io.mpolivakha.r2dbc.web_client_example.R2DBCodeSample;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class R2DBCodeSampleTest {

  @Mock
  private AgreementRepository agreementRepository;

  @Test
  void test() throws IOException, InterruptedException {
    MockWebServer mockWebServer = new MockWebServer();

    mockWebServer.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody("""
                          {
                            "type": "STANDARD",
                            "id" : "123",
                            "expiresAt" : "2099-12-03T10:15:30+01:00"
                          }
            """.trim()));

    new R2DBCodeSample("http://localhost:" + mockWebServer.getPort(), agreementRepository).findAgreement("123", null);

    Mockito
        .verify(agreementRepository, Mockito.timeout(50000).times(1))
        .renewAgreement(Mockito.any());
  }
}