package uk.co.dave;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import uk.co.dave.controller.TestDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MyApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(stubsMode = StubsMode.CLASSPATH, ids = {"uk.co.dave:secure-message-service:+:stubs"})
@Disabled
public class TestControllerHardcodedEndpointIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testGet() {
    this.webTestClient.get().uri("/tests").exchange().expectStatus().isOk();
  }

  @Test
  public void testPost() {
    MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
    multipartBodyBuilder.part("subject", "subject");
    multipartBodyBuilder.part("body", "body");
    multipartBodyBuilder.part("topic", "topic");
    multipartBodyBuilder.part("test", new TestDto("SomeData"), MediaType.APPLICATION_JSON);


    Flux<DataBuffer> content = Flux.empty();
    multipartBodyBuilder.asyncPart("files", content, DataBuffer.class).headers(h -> {

      h.setContentDispositionFormData("file", "test.pdf");
      h.setContentType(MediaType.APPLICATION_PDF);
    });

    this.webTestClient.post().uri("/tests").body(BodyInserters.fromMultipartData(multipartBodyBuilder.build())).header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE).exchange()
        .expectStatus().isOk().expectBody().json("{status:\"SomeData\"}");
  }

}
