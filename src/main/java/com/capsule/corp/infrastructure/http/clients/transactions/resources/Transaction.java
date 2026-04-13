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

  private UUID transactionId;

  private UUID accountNumber;
  private LocalDateTime timestamp;
  private BigDecimal amount;
  private String initiator;

  private String transactionType;
  private String entryType;
}
