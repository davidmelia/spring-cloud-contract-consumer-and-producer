package uk.co.dave.controller;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class TestController {

  // @Autowired
  public WebClient webClient;

  @PostMapping("/hardcoded-tests")
  public TestDto hardcodedMultipart(@RequestPart(value = "files", required = false) Flux<FilePart> filePartFlux, @RequestPart(name = "test") TestDto test) {
    return new TestDto(test.getStatus());
  }

  @PostMapping("/tests")
  public TestDto createNew(@RequestPart(value = "files", required = false) Flux<FilePart> filePartFlux, @RequestPart(name = "test") TestDto test) {
    return new TestDto(test.getStatus());
  }

  @GetMapping("/tests")
  public TestDto get() {
    return new TestDto("GET");
  }

}

