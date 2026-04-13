package com.capsule.corp.infrastructure.http.controllers.client.resources.response;

import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientSummaryResponse {
  private String cifNumber;
  private String firstName;
  private String lastName;
  private ClientStatus clientStatus;
}
