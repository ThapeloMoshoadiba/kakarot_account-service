package com.capsule.corp.infrastructure.http.clients.transactions;

import com.capsule.corp.common.config.AppConfiguration;
import com.capsule.corp.infrastructure.http.clients.transactions.resources.TransactionRequest;
import com.capsule.corp.infrastructure.http.clients.transactions.resources.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceClient {

  private final AppConfiguration.TransactionServiceConfig config;
  private final RestClient transactionServiceRestClient;

  public void openAccountTransaction(final TransactionRequest transactionRequest) {
    try {
      ResponseEntity<TransactionResponse> response =
          transactionServiceRestClient
              .method(HttpMethod.PUT)
              .uri("%s%s".formatted(config.getBaseUrl(), config.getOpenEndpoint()))
              .body(transactionRequest)
              .contentType(MediaType.APPLICATION_JSON)
              .retrieve()
              .toEntity(new ParameterizedTypeReference<>() {});

      if (response.getStatusCode().is2xxSuccessful()) {
        log.info("Opening transaction successfully executed");
      }
    } catch (Exception e) {
      log.error(
          "Error Running Opening Balance Transaction: [{}] [{}]",
          transactionRequest.getAccountNumber(),
          e.getMessage());
    }
  }
}
