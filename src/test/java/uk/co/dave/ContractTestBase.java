package uk.co.dave;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern;
import io.restassured.RestAssured;
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MyApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class ContractTestBase {

  private final ResourceLoader resourceLoader = new DefaultResourceLoader();

  @LocalServerPort
  private int port;


  @BeforeEach
  public void setup() throws IOException {
    RestAssured.baseURI = "http://localhost:" + port;

    // mocks
    // mockElasticSearchCall("intialElasticInstrumentIdsCall.request.json",
    // "intialElasticInstrumentIdsCall.response.json");
    // mockElasticSearchCall("finalElasticInstrumentIdsCallWithSearchAfter.request.json",
    // "finalElasticInstrumentIdsCallWithSearchAfter.response.json");

  }


  private void mockElasticSearchCall(String elasticRequestFilename, String elasticResponseFilename) throws IOException {
    byte[] response = StreamUtils.copyToByteArray(resourceLoader.getResource("mock-json/instrument-symbology/" + elasticResponseFilename).getInputStream());
    EqualToJsonPattern body = new EqualToJsonPattern(
        StreamUtils.copyToString(resourceLoader.getResource("mock-json/instrument-symbology/" + elasticRequestFilename).getInputStream(), Charset.forName("UTF-8")), Boolean.FALSE, Boolean.FALSE);
    stubFor(post(urlMatching("/searchable_instruments/_search")).withRequestBody(body).willReturn(aResponse().withHeader("Content-Type", "application/json").withStatus(200).withBody(response)));
  }


}
