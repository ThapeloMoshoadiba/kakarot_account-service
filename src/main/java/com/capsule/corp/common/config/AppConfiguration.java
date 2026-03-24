package com.capsule.corp.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {

  @Data
  @Component
  @ConfigurationProperties(prefix = "app.integration.transaction-service")
  public class TransactionServiceConfig extends IntegrationConfig {
    private String openEndpoint;
  }
}
