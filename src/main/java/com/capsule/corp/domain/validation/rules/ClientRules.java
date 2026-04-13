package com.capsule.corp.domain.validation.rules;

import static com.capsule.corp.infrastructure.http.resources.Constants.CANNOT_COMMUNICATE_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_CLIENT_STATUS_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_ID_NUMBER_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.PRESENT_CLIENT_MESSAGE;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientRules {
  private final ClientRepository clientRepository;

  public void canCreateClient(CreateClientRequest request) {
    final String id = request.getIdNumber();

    log.info("Running Create Client Rules for [{}]", id);
    isClient(id);
    isValidId(id, request.getDateOfBirth());
    canCommunicate(request);

    log.info("Create Client Rules Passed for [{}]", id);
  }

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
      throw new BusinessRuleException(INVALID_CLIENT_STATUS_MESSAGE);
    }
  }

  private void isClientBlocked(ClientDetails client) {
    if (!(client.getClientStatus() == ClientStatus.BLOCKED)) {
      throw new BusinessRuleException(INVALID_CLIENT_STATUS_MESSAGE);
    }
  }

  private void isClientNotBlocked(ClientDetails client) {
    if (client.getClientStatus() == ClientStatus.BLOCKED) {
      throw new BusinessRuleException(INVALID_CLIENT_STATUS_MESSAGE);
    }
  }

  private void isClient(String idNumber) {
    Optional<ClientDetails> client = clientRepository.findByIdNumber(idNumber);
    if (client.isPresent()) {
      throw new BusinessRuleException(PRESENT_CLIENT_MESSAGE);
    }
  }

  private void canCommunicate(CreateClientRequest request) {
    if (StringUtils.isBlank(request.getCellphoneNumber())
        && StringUtils.isBlank(request.getEmail())) {
      throw new BusinessRuleException(CANNOT_COMMUNICATE_MESSAGE);
    }
  }

  private void isValidId(String idNumber, LocalDate dateOfBirth) {
    if (!isValidDateOfBirth(idNumber, dateOfBirth) || !luhnCheck(idNumber)) {
      throw new BusinessRuleException(INVALID_ID_NUMBER_MESSAGE);
    }
  }

  private boolean isValidDateOfBirth(String id, LocalDate dateOfBirth) {
    return (id.substring(0, 6)).equals(dateOfBirth.format(DateTimeFormatter.ofPattern("yyMMdd")));
  }

  private boolean luhnCheck(String id) {
    int sum = 0;
    boolean alternate = false;

    for (int i = id.length() - 1; i >= 0; i--) {
      int n = id.charAt(i) - '0';

      if (alternate) {
        n *= 2;
        if (n > 9) n -= 9;
      }

      sum += n;
      alternate = !alternate;
    }

    return sum % 10 == 0;
  }
}
