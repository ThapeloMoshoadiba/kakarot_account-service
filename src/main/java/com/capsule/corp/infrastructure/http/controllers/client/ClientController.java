package com.capsule.corp.infrastructure.http.controllers.client;

import com.capsule.corp.domain.service.ClientService;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.BasicClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.UpdateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-service/client")
// @SecurityRequirement(name = "Bearer Authentication (JWT)")
@Tag(name = "Client Service", description = "Handles Clients")
public class ClientController {

  // we need a JWT token to ensure that the person making calls on these endpoints is allowed to do
  // so (e.g., an employee)
  // only an employee should be able to use these endpoints. Clients can't be allowed to manipulate
  // their own or other clients' data

  private final ClientService clientService;

  @Operation(summary = "Create Client")
  @PutMapping
  public ClientSummaryResponse createClient(
      @RequestBody final CreateClientRequest createClientRequest) {
    return clientService.createClient(createClientRequest);
  }

  @Operation(summary = "Retrieve Client Details")
  @GetMapping
  public ClientDetailedResponse getClient(
      @RequestParam(value = "idNumber", required = false) final String idNumber,
      @RequestParam(value = "cifNumber", required = false) final String cifNumber) {
    return clientService.getClient(idNumber, cifNumber);
  }

  @Operation(summary = "Update Client Data")
  @PutMapping("/update")
  public ClientDetailedResponse updateClient(
      @RequestBody final UpdateClientRequest updateClientRequest) {
    return clientService.updateClient(updateClientRequest);
  }

  @Operation(summary = "Block Client")
  @PutMapping("/block")
  public ClientSummaryResponse blockClient(@RequestBody final BasicClientRequest clientRequest) {
    return clientService.blockClient(clientRequest);
  }

  @Operation(summary = "Unblock Client")
  @PutMapping("/unblock")
  public ClientSummaryResponse unblockClient(@RequestBody final BasicClientRequest clientRequest) {
    return clientService.unblockClient(clientRequest);
  }
}
