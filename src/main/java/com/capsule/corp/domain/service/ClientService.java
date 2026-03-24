package com.capsule.corp.domain.service;

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

  String failureReason;
  Optional<ClientDetails> client = Optional.empty();

  public ClientSummaryResponse createClient(final CreateClientRequest createClientRequest) {
    try {
      String cifNumber = cifGenerator.generateCifNumber();
      ClientDetails newClient = clientMapper.mapClientEntity(createClientRequest, cifNumber);

      clientRepository.save(newClient);
      log.info("Client Successfully Created with CIF [{}]", newClient.getCifNumber());

      return clientMapper.mapClientSummary(newClient);
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error("Unable to create client: [{}]", failureReason);
    }
    return ClientSummaryResponse.builder().success(false).reason(failureReason).build();
  }

  public ClientDetailedResponse getClient(String idNumber, String cifNumber) {
    try {
      if (idNumber != null) {
        client = clientRepository.findByIdNumber(idNumber);
      } else if (cifNumber != null) {
        client = clientRepository.findByCifNumber(cifNumber);
      }

      if (client.isPresent()) {
        return clientMapper.mapClientDetailed(client.get());
      }
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error("Unable to retrieve client: [{}]", failureReason);
    }
    return ClientDetailedResponse.builder().success(false).reason(failureReason).build();
  }

  public ClientDetailedResponse updateClient(final UpdateClientRequest updateClientRequest) {
    try {
      client = clientRepository.findByCifNumber(updateClientRequest.getCifNumber());
      if (client.isPresent()) {
        ClientDetails existingClient = client.get();
        clientRules.canClientBeUpdated(existingClient);
        client = updateValidation.validateUpdate(existingClient, updateClientRequest);

        if (client.isPresent()) {
          ClientDetails updatedClient = client.get();
          LocalDateTime updateTimeStamp = LocalDateTime.now();
          updatedClient.setUpdatedAt(updateTimeStamp);

          clientRepository.save(updatedClient);

          client = clientRepository.findByCifNumber(updateClientRequest.getCifNumber());
          if (client.isPresent() && (client.get().getUpdatedAt()).isEqual(updateTimeStamp)) {
            log.info("Client [{}] successfully updated", updateClientRequest.getCifNumber());
            return clientMapper.mapClientDetailed(client.get());
          }
        }
        return ClientDetailedResponse.builder()
            .success(false)
            .reason("No changes detected")
            .build();
      }
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error("Unable to update client details [{}]", failureReason);
    }
    return ClientDetailedResponse.builder().success(false).reason(failureReason).build();
  }

  public ClientSummaryResponse blockClient(BasicClientRequest clientRequest) {
    try {
      client = clientRepository.findByCifNumber(clientRequest.getCifNumber());

      if (client.isPresent()) {
        ClientDetails clientToBeBlocked = client.get();
        clientRules.canClientBeBlocked(clientToBeBlocked);

        clientToBeBlocked.setReasonForBlock(clientRequest.getReason());
        clientToBeBlocked.setClientStatus(ClientStatus.BLOCKED);
        clientToBeBlocked.setBlockedAt(LocalDateTime.now());

        clientRepository.save(clientToBeBlocked);
        client = clientRepository.findByCifNumber(clientRequest.getCifNumber());

        if (client.isPresent() && (client.get().getClientStatus() == ClientStatus.BLOCKED)) {
          log.info("Client [{}] successfully blocked", clientRequest.getCifNumber());
          // Block client's account too
          return clientMapper.mapClientSummary(client.get());
        }
      }
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error("Unable to block client [{}]: [{}]", clientRequest.getCifNumber(), failureReason);
    }
    return ClientSummaryResponse.builder().success(false).reason(failureReason).build();
  }

  public ClientSummaryResponse unblockClient(BasicClientRequest clientRequest) {
    try {
      client = clientRepository.findByCifNumber(clientRequest.getCifNumber());

      if (client.isPresent() && (client.get().getClientStatus() == ClientStatus.BLOCKED)) {
        ClientDetails clientToBeUnblocked = client.get();
        clientRules.canClientBeUnblocked(clientToBeUnblocked);

        clientToBeUnblocked.setReasonForUnblock(clientRequest.getReason());
        clientToBeUnblocked.setClientStatus(ClientStatus.ACTIVE);
        clientToBeUnblocked.setUnblockedAt(LocalDateTime.now());

        clientRepository.save(clientToBeUnblocked);
        client = clientRepository.findByCifNumber(clientRequest.getCifNumber());

        if (client.isPresent() && (client.get().getClientStatus() == ClientStatus.ACTIVE)) {
          log.info("Client [{}] successfully unblocked", clientRequest.getCifNumber());
          // Unblock client's account too
          return clientMapper.mapClientSummary(client.get());
        }
      }
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error("Unable to unblock client [{}]: [{}]", clientRequest.getCifNumber(), failureReason);
    }
    return ClientSummaryResponse.builder().success(false).reason(failureReason).build();
  }
}
