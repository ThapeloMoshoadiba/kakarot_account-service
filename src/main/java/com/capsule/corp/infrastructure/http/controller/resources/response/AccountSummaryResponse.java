package com.capsule.corp.infrastructure.http.controller.resources.response;

import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class AccountSummaryResponse {

    UUID accountNumber;
    AccountStatus accountStatus;
    BigDecimal initialCreditAmount;
    boolean success;
    String reason;
}
