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
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MyApplication.class, TestOverrideConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubsMode.CLASSPATH, ids = {"uk.co.dave:some-external-service:+:stubs"})
@AutoConfigureWebTestClient(timeout = "1m")
// @Disabled
public class TestControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private ResourceLoader resourceLoader;

  @Test
  public void oneAttachement() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "GQ");
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    this.webTestClient.post().uri("/api/1/customers/00000000000/accounts/0000000/integration-test-endpoint").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange().expectStatus().isOk();
  }

  @Test
  public void sevenPartsUploaded() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "GQ");
    multipartBodyBuilder.part("conversationId", "convId");
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    this.webTestClient.post().uri("/api/1/customers/00000000000/accounts/0000000/integration-test-endpoint").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange().expectStatus().isOk();
  }

  @Test
  public void eightPartsUploadedWhichIsAnError() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "GQ");
    multipartBodyBuilder.part("conversationId", "convId");
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    multipartBodyBuilder.part("files", resourceLoader.getResource("classpath:dave.txt"));
    this.webTestClient.post().uri("/api/1/customers/00000000000/accounts/0000000/integration-test-endpoint").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange().expectStatus().is5xxServerError().expectBody().json("{}");
  }
}
