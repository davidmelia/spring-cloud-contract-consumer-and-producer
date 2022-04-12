package uk.co.dave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.stubrunner.StubFinder;

public class TestOverrideConfiguration {

  @Autowired
  public TestOverrideConfiguration(StubFinder stubFinder, MyApplicationProperties properties) {
    properties.setExternalServiceUrl(stubFinder.findStubUrl("secure-message-service").toExternalForm());
  }
}

