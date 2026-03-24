package com.capsule.corp.infrastructure.http.controllers.account.resources.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenCreditAccountRequest {

  String cifNumber;
  BigDecimal creditAmount;
}
