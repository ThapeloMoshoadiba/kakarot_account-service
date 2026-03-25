package com.capsule.corp.domain.validation;

import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.UpdateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateValidation {

  public Optional<ClientDetails> validateUpdate(
      ClientDetails existingClient, UpdateClientRequest newClientData) {
    boolean isUpdateValid = false;

    if (newClientData.getClientStatus() != null
        && isClientStatusChanged(
            existingClient.getClientStatus(), newClientData.getClientStatus())) {
      existingClient.setClientStatus(newClientData.getClientStatus());
      isUpdateValid = true;
    }
    if (!StringUtils.isBlank(newClientData.getLastName())
        && isLastNameChanged(existingClient.getLastName(), newClientData.getLastName())) {
      existingClient.setLastName(newClientData.getLastName());
      isUpdateValid = true;
    }
    if (!StringUtils.isBlank(newClientData.getAddress())
        && isAddressChanged(existingClient.getAddress(), newClientData.getAddress())) {
      existingClient.setAddress(newClientData.getAddress());
      isUpdateValid = true;
    }
    if (!StringUtils.isBlank(newClientData.getCellphoneNumber())
        && isCellChanged(existingClient.getCellphoneNumber(), newClientData.getCellphoneNumber())) {
      existingClient.setCellphoneNumber(newClientData.getCellphoneNumber());
      isUpdateValid = true;
    }
    if (!StringUtils.isBlank(newClientData.getEmail())
        && isEmailChanged(existingClient.getEmail(), newClientData.getEmail())) {
      existingClient.setEmail(newClientData.getEmail());
      isUpdateValid = true;
    }
    if (newClientData.getCredit() != null
        && isCreditChanged(existingClient.getCredit(), newClientData.getCredit())) {
      existingClient.setCredit(newClientData.getCredit());
      isUpdateValid = true;
    }
    if (newClientData.getEmploymentStatus() != null
        && isEmploymentChanged(
            existingClient.getEmploymentStatus(), newClientData.getEmploymentStatus())) {
      existingClient.setEmploymentStatus(newClientData.getEmploymentStatus());
      isUpdateValid = true;
    }
    if (newClientData.getSourceOfFunds() != null
        && isSourceOfFundsChanged(
            existingClient.getSourceOfFunds(), newClientData.getSourceOfFunds())) {
      existingClient.setSourceOfFunds(newClientData.getSourceOfFunds());
      isUpdateValid = true;
    }
    if (newClientData.getVerifiedAnnualIncome() != null
        && isIncomeChanged(
            existingClient.getVerifiedAnnualIncome(), newClientData.getVerifiedAnnualIncome())) {
      existingClient.setVerifiedAnnualIncome(newClientData.getVerifiedAnnualIncome());
      isUpdateValid = true;
    }

    if (!isUpdateValid) {
      return Optional.empty();
    }

    return Optional.of(existingClient);
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
