package uk.co.dave;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import uk.co.dave.controller.TestDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MyApplication.class, TestOverrideConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubsMode.CLASSPATH, ids = {"uk.co.dave:secure-message-service:+:stubs"})
// @TestPropertySource(properties =
// {"uk.co.dave.external-service-url=http://localhost:${wiremock.server.port}/tests"})
@AutoConfigureWebTestClient(timeout = "1m")
// @Disabled
public class TestControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;


  // @Test
  public void tests() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "GQ");
    multipartBodyBuilder.part("files", new FileSystemResource("/Users/meliad2/git/spring-cloud-contract-consumer-and-producer/src/test/resources/dave.txt"));
    multipartBodyBuilder.part("test", new TestDto("SomeData"), MediaType.APPLICATION_JSON);
    this.webTestClient.post().uri("/tests").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build())).header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange()
        .expectStatus().isOk().expectBody().json("{status:\"SomeData\"}");
  }

  @Test
  public void tests1() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "GQ");
    multipartBodyBuilder.part("files", new FileSystemResource("/Users/meliad2/git/spring-cloud-contract-consumer-and-producer/src/test/resources/dave.txt"));
    this.webTestClient.post().uri("/api/1/customers/00000000000/accounts/0000000/tests1").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange().expectStatus().isOk();
  }

}
