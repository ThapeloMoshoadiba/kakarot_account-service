package com.capsule.corp.domain.service;

import static com.capsule.corp.infrastructure.http.resources.Constants.CLIENT_NOT_FOUND_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NO_CHANGES_DETECTED_MESSAGE;

import com.capsule.corp.common.exception.ClientNotFoundException;
import com.capsule.corp.common.exception.InvalidUpdateException;
import com.capsule.corp.domain.mapper.ClientMapper;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.domain.validation.CifGenerator;
import com.capsule.corp.domain.validation.UpdateValidation;
import com.capsule.corp.domain.validation.rules.ClientRules;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.BasicClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.UpdateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

  private final ClientRules clientRules;
  private final CifGenerator cifGenerator;
  private final ClientMapper clientMapper;
  private final ClientRepository clientRepository;
  private final UpdateValidation updateValidation;

  private Optional<ClientDetails> client;

  public ResponseEntity<ClientSummaryResponse> createClient(
      final CreateClientRequest createClientRequest) {
    // Check if Client Already Exists. Have some create client rules (e.g., id number check in DB
    // etc.)
    String cifNumber = cifGenerator.generateCifNumber();
    ClientDetails newClient = clientMapper.mapClientEntity(createClientRequest, cifNumber);

    clientRepository.save(newClient);
    return ResponseEntity.ok(clientMapper.mapClientSummary(newClient));
  }

  public ResponseEntity<ClientDetailedResponse> getClient(String idNumber, String cifNumber) {
    if (idNumber != null) {
      client = clientRepository.findByIdNumber(idNumber);
    } else if (cifNumber != null) {
      client = clientRepository.findByCifNumber(cifNumber);
    }

    if (client.isEmpty()) {
      throw new ClientNotFoundException(CLIENT_NOT_FOUND_MESSAGE);
    }
    return ResponseEntity.ok(clientMapper.mapClientDetailed(client.get()));
  }

  public ResponseEntity<ClientDetailedResponse> updateClient(
      final UpdateClientRequest updateClientRequest) {
    ClientDetails existingClient = getClient(updateClientRequest.getCifNumber());
    clientRules.canClientBeUpdated(existingClient);
    client = updateValidation.validateUpdate(existingClient, updateClientRequest);
    if (client.isEmpty()) {
      throw new InvalidUpdateException(NO_CHANGES_DETECTED_MESSAGE);
    }

    ClientDetails updatedClient = client.get();
    LocalDateTime updateTimeStamp = LocalDateTime.now();
    updatedClient.setUpdatedAt(updateTimeStamp);

    clientRepository.save(updatedClient);
    return ResponseEntity.ok(clientMapper.mapClientDetailed(updatedClient));
  }

  public ResponseEntity<ClientSummaryResponse> blockClient(BasicClientRequest clientRequest) {
    ClientDetails clientToBeBlocked = getClient(clientRequest.getCifNumber());
    clientRules.canClientBeBlocked(clientToBeBlocked);

    clientToBeBlocked.setReasonForBlock(clientRequest.getReason());
    clientToBeBlocked.setClientStatus(ClientStatus.BLOCKED);
    clientToBeBlocked.setBlockedAt(LocalDateTime.now());

    clientRepository.save(clientToBeBlocked);

    // TODO: Block Client's Account Too
    return ResponseEntity.ok(clientMapper.mapClientSummary(clientToBeBlocked));
  }

  public ResponseEntity<ClientSummaryResponse> unblockClient(BasicClientRequest clientRequest) {
    ClientDetails clientToBeUnblocked = getClient(clientRequest.getCifNumber());
    clientRules.canClientBeUnblocked(clientToBeUnblocked);

    clientToBeUnblocked.setReasonForUnblock(clientRequest.getReason());
    clientToBeUnblocked.setClientStatus(ClientStatus.ACTIVE);
    clientToBeUnblocked.setUnblockedAt(LocalDateTime.now());

    clientRepository.save(clientToBeUnblocked);

    // TODO: Unblock Client's Account Too
    return ResponseEntity.ok(clientMapper.mapClientSummary(clientToBeUnblocked));
  }

  private ClientDetails getClient(String cifNumber) {
    client = clientRepository.findByCifNumber(cifNumber);
    if (client.isEmpty()) {
      throw new ClientNotFoundException(CLIENT_NOT_FOUND_MESSAGE);
    }

    return client.get();
  }
}
