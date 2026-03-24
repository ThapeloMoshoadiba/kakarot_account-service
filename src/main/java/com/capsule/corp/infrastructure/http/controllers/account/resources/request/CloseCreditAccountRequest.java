package com.capsule.corp.infrastructure.http.controllers.account.resources.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseCreditAccountRequest {

  UUID accountNumber;
  String reasonForClose;
}
