package com.capsule.corp.domain.validation;

import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.UpdateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateValidation {

  private final ClientRepository clientRepository;

  public boolean isUpdateValid(ClientDetails existingClient, UpdateClientRequest newClientData) {
    if (isClientStatusChanged(existingClient.getClientStatus(), newClientData.getClientStatus())) {
      return true;
    }
    if (isLastNameChanged(existingClient.getLastName(), newClientData.getLastName())) {
      return true;
    }
    if (isAddressChanged(existingClient.getAddress(), newClientData.getAddress())) {
      return true;
    }
    if (isCellChanged(existingClient.getCellphoneNumber(), newClientData.getCellphoneNumber())) {
      return true;
    }
    if (isEmailChanged(existingClient.getEmail(), newClientData.getEmail())) {
      return true;
    }
    if (isCreditChanged(existingClient.getCredit(), newClientData.getCredit())) {
      return true;
    }
    if (isEmploymentChanged(
        existingClient.getEmploymentStatus(), newClientData.getEmploymentStatus())) {
      return true;
    }
    if (isSourceOfFundsChanged(
        existingClient.getSourceOfFunds(), newClientData.getSourceOfFunds())) {
      return true;
    }
    if (isIncomeChanged(
        existingClient.getVerifiedAnnualIncome(), newClientData.getVerifiedAnnualIncome())) {
      return true;
    }
    return false;
  }

  private boolean isClientStatusChanged(
      ClientStatus existingClientStatus, ClientStatus newClientStatus) {
    return existingClientStatus != newClientStatus;
  }

  private boolean isLastNameChanged(String existingLastName, String newLastName) {
    return !Objects.equals(existingLastName, newLastName);
  }

  private boolean isAddressChanged(String existingAddress, String newAddress) {
    return !Objects.equals(existingAddress, newAddress);
  }

  private boolean isCellChanged(String existingCell, String newCell) {
    return !Objects.equals(existingCell, newCell);
  }

  private boolean isEmailChanged(String existingEmail, String newEmail) {
    return !Objects.equals(existingEmail, newEmail);
  }

  private boolean isCreditChanged(CreditStanding existingCredit, CreditStanding newCredit) {
    return existingCredit != newCredit;
  }

  private boolean isEmploymentChanged(
      EmploymentStatus existingEmploymentStatus, EmploymentStatus newEmploymentStatus) {
    return existingEmploymentStatus != newEmploymentStatus;
  }

  private boolean isSourceOfFundsChanged(SourceOfFunds existingSof, SourceOfFunds newSof) {
    return existingSof != newSof;
  }

  private boolean isIncomeChanged(BigDecimal existingIncome, BigDecimal newIncome) {
    return !Objects.equals(existingIncome, newIncome);
  }
}
