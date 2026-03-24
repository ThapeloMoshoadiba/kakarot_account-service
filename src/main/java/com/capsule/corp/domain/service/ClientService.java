package com.capsule.corp.domain.service;

import com.capsule.corp.domain.mapper.ClientMapper;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.domain.validation.CifGenerator;
import com.capsule.corp.domain.validation.UpdateValidation;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.RemoveClientRequest;
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

  private final ClientRepository clientRepository;
  private final ClientMapper clientMapper;
  private final CifGenerator cifGenerator;
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

        if (updateValidation.isUpdateValid(existingClient, updateClientRequest)) {
          LocalDateTime updateTimeStamp = LocalDateTime.now();
          clientRepository.save(clientMapper.mapClientEntity(updateClientRequest, updateTimeStamp));

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

  public ClientSummaryResponse blockClient(RemoveClientRequest removeClientRequest) {
    try {
      client = clientRepository.findByCifNumber(removeClientRequest.getCifNumber());

      if (client.isPresent()) {
        clientRepository.save(clientMapper.mapClientEntity(removeClientRequest));
        client = clientRepository.findByCifNumber(removeClientRequest.getCifNumber());

        if (client.isPresent() && (client.get().getClientStatus() == ClientStatus.BLOCKED)) {
          log.info("Client [{}] successfully blocked", removeClientRequest.getCifNumber());
          return clientMapper.mapClientSummary(client.get());
        }
      }
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error(
          "Unable to block client [{}]: [{}]", removeClientRequest.getCifNumber(), failureReason);
    }
    return ClientSummaryResponse.builder().success(false).reason(failureReason).build();
  }
}
