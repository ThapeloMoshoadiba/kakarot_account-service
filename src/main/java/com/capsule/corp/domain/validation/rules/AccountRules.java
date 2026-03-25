package com.capsule.corp.domain.validation.rules;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.controllers.account.resources.Account;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRules {
  private final ClientRepository clientRepository;
  private final AccountRepository accountRepository;

  public void canAccountBeOpened(
      ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest) {
    log.info("Running Open Account Rules for client [{}]", openCreditAccountRequest.getCifNumber());
    hasCreditAccount(openCreditAccountRequest.getCifNumber());
    isOfAge(client.getDateOfBirth());
    hasIncome(client.getSourceOfFunds());
    isCreditWorthy(client, openCreditAccountRequest);
    isClientActive(client);

    log.info("Open Account Rules Passed for client [{}]", openCreditAccountRequest.getCifNumber());
  }

  public void canAccountBeClosed(Account account, BigDecimal amount, BigDecimal balance) {
    log.info("Running Close Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    hasBalance(amount, balance);
    notClosed(account.getAccountStatus());

    log.info("Close Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  public void canAccountBeBlocked(Account account) {
    log.info("Running Block Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    notBlocked(account.getAccountStatus());

    log.info("Block Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  public void canAccountBeUnblocked(Account account) {
    log.info("Running Unblock Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    isBlocked(account.getAccountStatus());

    log.info("Unblock Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  private void hasClient(String cifNumber) {
    if (clientRepository.findByCifNumber(cifNumber).isEmpty()) {
      throw new BusinessRuleException("Credit Account Present for non-existent Client");
    }
    log.info("Client Exists");
  }

  private void hasBalance(BigDecimal amount, BigDecimal balance) {
    /*
    if (balance <= 0 || amount < balance) {
        throw new BusinessRuleException("Credit Account must be at a Zero balance to Close Account");
    }
    */
    log.info("Balance Rules Assessed");
  }

  private void notBlocked(AccountStatus status) {
    if (status == AccountStatus.BLOCKED) {
      throw new BusinessRuleException("Credit Account Already Blocked");
    }
    log.info("Credit Account Not Yet Blocked");
  }

  private void isBlocked(AccountStatus status) {
    if (!(status == AccountStatus.BLOCKED)) {
      throw new BusinessRuleException("Credit Account Must Be Blocked");
    }
    log.info("Credit Account is Blocked");
  }

  private void notClosed(AccountStatus status) {
    if (status == AccountStatus.CLOSED) {
      throw new BusinessRuleException("Credit Account Already Closed");
    }
    log.info("Credit Account Not Yet Closed");
  }

  private void hasCreditAccount(String cifNumber) {
    Optional<List<Account>> accounts = accountRepository.findByCifNumber(cifNumber);
    if (accounts.isPresent()
        && accounts.get().stream().anyMatch(acc -> acc.getAccountStatus() == AccountStatus.OPEN)) {
      throw new BusinessRuleException("Open Credit Account already exists for Client");
    }
    log.info("Client does not have an Open Credit Account");
  }

  private void isOfAge(LocalDate dob) {
    if (Period.between(dob, LocalDate.now()).getYears() < 18) {
      throw new BusinessRuleException("Must Be 18 or older");
    }
    log.info("Client is of age");
  }

  private void isCreditWorthy(
      ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest) {
    if (client.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
      throw new BusinessRuleException("Must be employed");
    }
    if (client.getCredit() == CreditStanding.BAD
        || client.getCredit() == CreditStanding.NEEDS_SUPPORT) {
      throw new BusinessRuleException("Client has poor credit record");
    }
    if (!((percentOf(client.getVerifiedAnnualIncome(), openCreditAccountRequest.getCreditAmount()))
        <= 25)) {
      throw new BusinessRuleException("Credit must be 25% or less of total annual income");
    }
    log.info("Client is credit-worthy");
  }

  private void hasIncome(SourceOfFunds sourceOfFunds) {
    if (sourceOfFunds == SourceOfFunds.NONE) {
      throw new BusinessRuleException("Must have an income");
    }
    log.info("Client has income");
  }

  private void isClientActive(ClientDetails client) {
    if (!(client.getClientStatus() == ClientStatus.ACTIVE)) {
      throw new BusinessRuleException("Client must be in ACTIVE status");
    }
    log.info("Client is ACTIVE");
  }

  private static int percentOf(BigDecimal base, BigDecimal value) {
    return (value.divide(base, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)))
        .intValue();
  }
}
