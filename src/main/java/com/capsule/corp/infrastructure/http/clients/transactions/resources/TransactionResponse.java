package com.capsule.corp.infrastructure.http.clients.transactions.resources;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

  UUID transactionId;
  boolean success;
}
