package uk.co.dave.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uk.co.dave.MyApplicationProperties;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

  public final WebClient webClient;

  public TestController(WebClient.Builder builder, MyApplicationProperties props) {
    this.webClient = builder.baseUrl(props.getExternalServiceUrl()).build();
  }

  @PostMapping("/1/customers/{customerId}/accounts/{accountId}/endpoint-for-contract-tests")
  public Mono<ResponseEntity<Void>> hardcodedMultipart(@PathVariable("customerId") String customerId, @PathVariable("accountId") String accountId, @RequestPart FormFieldPart subject,
      @RequestPart FormFieldPart body, @RequestPart FormFieldPart topic, @RequestPart(value = "files", required = false) Flux<FilePart> filePartFlux) {
    log.info("hardcodedMultipart");

    return filePartFlux.flatMap(filePart -> {
      log.info("{}", filePart.name());

      return filePart.content().collectList().map(bufferList -> {
        log.info("bufferList size={}", bufferList.size());
        return bufferList;
      });

    }).collectList().thenReturn(new ResponseEntity<Void>(HttpStatus.CREATED));
  }

  @PostMapping("/1/customers/{customerId}/accounts/{accountId}/tests")
  public Mono<TestDto> createNew(@RequestPart(value = "files", required = false) Flux<FilePart> filePartFlux, @RequestPart(name = "test") TestDto test) {

    Mono<TestDto> result = filePartFlux.collectList().flatMap(fileParts -> {
      var builder = new MultipartBodyBuilder();
      for (var file : fileParts) {
        builder.part("files", file);
      }
      builder.part("test", test, MediaType.APPLICATION_JSON);
      return webClient.post().uri("/tests").contentType(MediaType.MULTIPART_FORM_DATA).body(BodyInserters.fromMultipartData(builder.build())).retrieve().bodyToMono(TestDto.class);
    });

    return result;
  }

  @PostMapping("/1/customers/{customerId}/accounts/{accountId}/tests1")
  public Mono<TestDto> createNew1(@PathVariable("customerId") String customerId, @PathVariable("accountId") String accountId, @RequestPart FormFieldPart subject, @RequestPart FormFieldPart body,
      @RequestPart FormFieldPart topic, @RequestPart(value = "files", required = false) Flux<FilePart> filePartFlux) {

    Mono<TestDto> result = filePartFlux.collectList().flatMap(fileParts -> {
      var builder = new MultipartBodyBuilder();

      builder.part("subject", subject.value());
      builder.part("body", body.value());
      builder.part("topic", topic.value());

      for (var file : fileParts) {
        builder.part("files", file);
      }
      return webClient.post().uri("/api/1/customers/{customerId}/accounts/{accountId}/tests1", customerId, accountId).contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData(builder.build())).retrieve().bodyToMono(TestDto.class);
    });

    return result;
  }

  @GetMapping("/tests")
  public TestDto get() {
    return new TestDto("GET");
  }

}

