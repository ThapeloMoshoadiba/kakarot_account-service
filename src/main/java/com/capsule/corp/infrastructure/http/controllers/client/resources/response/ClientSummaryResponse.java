package com.capsule.corp.infrastructure.http.controllers.client.resources.response;

import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientSummaryResponse {

  String cifNumber;
  String firstName;
  String lastName;
  ClientStatus clientStatus;
  boolean success;
  String reason;
}
