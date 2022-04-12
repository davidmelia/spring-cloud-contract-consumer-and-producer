package uk.co.dave;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "uk.co.dave")
@Data
public class MyApplicationProperties {

  private String externalServiceUrl;
}
