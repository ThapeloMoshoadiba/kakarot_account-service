package com.capsule.corp.infrastructure.http.controllers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalErrorResponse {

  @Builder.Default private boolean success = false;
  private String reason;
}
