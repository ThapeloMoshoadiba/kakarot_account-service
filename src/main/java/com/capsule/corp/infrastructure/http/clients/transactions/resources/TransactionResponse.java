package com.capsule.corp.infrastructure.http.clients.transactions.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    UUID transactionId;
    boolean success;
}
