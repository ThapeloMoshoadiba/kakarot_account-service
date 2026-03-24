package com.capsule.corp.domain.validation.rules;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientRules {
  private final ClientRepository clientRepository;

  public void canClientBeUpdated(ClientDetails client) {
    log.info("Running Update Client Rules for client [{}]", client.getCifNumber());
    isClientActive(client);

    log.info("Update Client Rules Passed for client [{}]", client.getCifNumber());
  }

  public void canClientBeBlocked(ClientDetails client) {
    log.info("Running Block Client Rules for client [{}]", client.getCifNumber());
    isClientNotBlocked(client);

    log.info("Block Client Rules Passed for client [{}]", client.getCifNumber());
  }

  public void canClientBeUnblocked(ClientDetails client) {
    log.info("Running Unblock Client Rules for client [{}]", client.getCifNumber());
    isClientBlocked(client);

    log.info("Unblock Client Rules Passed for client [{}]", client.getCifNumber());
  }

  private void isClientActive(ClientDetails client) {
    if (!(client.getClientStatus() == ClientStatus.ACTIVE)) {
      throw new BusinessRuleException("Client must be in ACTIVE status");
    }
    log.info("Client is ACTIVE");
  }

  private void isClientBlocked(ClientDetails client) {
    if (!(client.getClientStatus() == ClientStatus.BLOCKED)) {
      throw new BusinessRuleException("Client must be in BLOCKED status");
    }
    log.info("Client is BLOCKED");
  }

  private void isClientNotBlocked(ClientDetails client) {
    if (client.getClientStatus() == ClientStatus.BLOCKED) {
      throw new BusinessRuleException("Client already in BLOCKED status");
    }
    log.info("Client is not BLOCKED");
  }
}
