package com.capsule.corp.infrastructure.http.clients.transactions.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

  UUID transactionId;

  UUID accountNumber;
  LocalDateTime timestamp;
  BigDecimal amount;
  String initiator;

  String transactionType;
  String entryType;
}
