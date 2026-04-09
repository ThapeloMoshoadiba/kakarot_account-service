package com.capsule.corp.infrastructure.http.controllers.account.resources.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicAccountRequest {

  UUID accountNumber;
  String reason;
}
