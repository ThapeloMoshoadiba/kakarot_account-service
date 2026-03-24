package com.capsule.corp.infrastructure.http.controllers.client.resources.response;

import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDetailedResponse {
  ClientDetails clientDetails;
  boolean success;
  String reason;
}
