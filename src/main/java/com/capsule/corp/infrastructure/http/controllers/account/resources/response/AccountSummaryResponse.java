package com.capsule.corp.infrastructure.http.controllers.account.resources.response;

import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountSummaryResponse {

  UUID accountNumber;
  AccountStatus accountStatus;
  BigDecimal initialCreditAmount;
  boolean success;
  String reason;
}
