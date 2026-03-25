package com.capsule.corp.domain.mapper;

import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.BasicClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    imports = {UUID.class, LocalDate.class, LocalDateTime.class, ClientStatus.class})
public interface ClientMapper {

  @Mapping(target = "clientId", expression = "java(UUID.randomUUID())")
  @Mapping(target = "cifNumber", source = "cifNumber")
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
  @Mapping(target = "clientStatus", expression = "java(ClientStatus.ACTIVE)")
  @Mapping(target = "title", source = "createClientRequest.title")
  @Mapping(target = "gender", source = "createClientRequest.gender")
  @Mapping(target = "idNumber", source = "createClientRequest.idNumber")
  @Mapping(target = "dateOfBirth", source = "createClientRequest.dateOfBirth")
  @Mapping(target = "firstName", source = "createClientRequest.firstName")
  @Mapping(target = "middleName", source = "createClientRequest.middleName")
  @Mapping(target = "lastName", source = "createClientRequest.lastName")
  @Mapping(target = "address", source = "createClientRequest.address")
  @Mapping(target = "cellphoneNumber", source = "createClientRequest.cellphoneNumber")
  @Mapping(target = "email", source = "createClientRequest.email")
  @Mapping(target = "credit", source = "createClientRequest.credit")
  @Mapping(target = "employmentStatus", source = "createClientRequest.employmentStatus")
  @Mapping(target = "sourceOfFunds", source = "createClientRequest.sourceOfFunds")
  @Mapping(target = "verifiedAnnualIncome", source = "createClientRequest.verifiedAnnualIncome")
  ClientDetails mapClientEntity(CreateClientRequest createClientRequest, String cifNumber);

  @Mapping(target = "clientDetails", source = "clientDetails")
  @Mapping(target = "success", constant = "true")
  ClientDetailedResponse mapClientDetailed(ClientDetails clientDetails);

  @Mapping(target = "cifNumber", source = "clientDetails.cifNumber")
  @Mapping(target = "firstName", source = "clientDetails.firstName")
  @Mapping(target = "lastName", source = "clientDetails.lastName")
  @Mapping(target = "clientStatus", source = "clientDetails.clientStatus")
  @Mapping(target = "success", constant = "true")
  ClientSummaryResponse mapClientSummary(ClientDetails clientDetails);

  @Mapping(target = "cifNumber", source = "cifNumber")
  @Mapping(target = "reason", source = "reason")
  BasicClientRequest mapRemoveClientRequest(String cifNumber, String reason);
}
