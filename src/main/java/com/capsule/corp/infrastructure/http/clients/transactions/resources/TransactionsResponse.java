package com.capsule.corp.infrastructure.http.clients.transactions.resources;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionsResponse {
  List<Transaction> transactions;
  BigDecimal balance;

  boolean success;
  String reason;
}
