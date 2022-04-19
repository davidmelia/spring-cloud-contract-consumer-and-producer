package uk.co.dave;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CodecConfig implements WebFluxConfigurer {
  @Override
  public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {

    var partReader = new DefaultPartHttpMessageReader();
    partReader.setMaxParts(7);
    partReader.setMaxHeadersSize(9216);
    partReader.setEnableLoggingRequestDetails(true);

    var multipartReader = new MultipartHttpMessageReader(partReader);

    configurer.customCodecs().register(multipartReader);

  }
}
