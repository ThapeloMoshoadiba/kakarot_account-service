package com.capsule.corp.infrastructure.http.clients.transactions.resources;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
  UUID transactionId;
  UUID accountNumber;
  BigDecimal amount;
}
