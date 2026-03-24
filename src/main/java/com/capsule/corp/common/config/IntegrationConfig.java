package com.capsule.corp.common.config;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class IntegrationConfig {

  private String baseUrl;
  private int readTimeout = 60000;
  private int connectionTimeout = 60000;
  private int maxRetries = 1;
}
