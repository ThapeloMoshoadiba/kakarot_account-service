package com.capsule.corp.infrastructure.http.controllers.client.resources.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicClientRequest {

  String cifNumber;
  String reason;
}
