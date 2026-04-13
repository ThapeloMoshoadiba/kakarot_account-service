package com.capsule.corp.domain.validation.rules;

import static com.capsule.corp.infrastructure.http.resources.Constants.CLIENT_NOT_FOUND_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_ACCOUNT_STATUS_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_AGE_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.INVALID_CLIENT_STATUS_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.PRESENT_ACCOUNT_MESSAGE;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.clients.transactions.TransactionServiceClient;
import com.capsule.corp.infrastructure.http.clients.transactions.resources.TransactionsResponse;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountRules {
  private final ClientRepository clientRepository;
  private final AccountRepository accountRepository;
  private final TransactionServiceClient transactionServiceClient;

  public void canAccountBeOpened(
      final ClientDetails client, final OpenCreditAccountRequest openCreditAccountRequest) {
    log.info("Running Open Account Rules for client [{}]", openCreditAccountRequest.getCifNumber());
    hasCreditAccount(openCreditAccountRequest.getCifNumber());
    isOfAge(client.getDateOfBirth());
    hasIncome(client.getSourceOfFunds());
    isCreditWorthy(client, openCreditAccountRequest);
    isClientActive(client);

    log.info("Open Account Rules Passed for client [{}]", openCreditAccountRequest.getCifNumber());
  }

  public void canAccountBeClosed(final Account account) {
    log.info("Running Close Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    notClosed(account.getAccountStatus());
    hasBalance(account.getAccountNumber());
    notOpen(account.getAccountStatus());

    log.info("Close Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  public void canAccountBeBlocked(final Account account) {
    log.info("Running Block Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    notBlocked(account.getAccountStatus());
    notOpen(account.getAccountStatus());

    log.info("Block Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  public void canAccountBeUnblocked(final Account account) {
    log.info("Running Unblock Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    isBlocked(account.getAccountStatus());

    log.info("Unblock Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  private void hasClient(final String cifNumber) {
    if (clientRepository.findByCifNumber(cifNumber).isEmpty()) {
      throw new BusinessRuleException(CLIENT_NOT_FOUND_MESSAGE);
    }
  }

  private void notOpen(final AccountStatus status) {
    if (!(status == AccountStatus.OPEN)) {
      throw new BusinessRuleException(INVALID_ACCOUNT_STATUS_MESSAGE);
    }
  }

  private void notBlocked(final AccountStatus status) {
    if (status == AccountStatus.BLOCKED) {
      throw new BusinessRuleException(INVALID_ACCOUNT_STATUS_MESSAGE);
    }
  }

  private void isBlocked(final AccountStatus status) {
    if (!(status == AccountStatus.BLOCKED)) {
      throw new BusinessRuleException(INVALID_ACCOUNT_STATUS_MESSAGE);
    }
  }

  private void notClosed(final AccountStatus status) {
    if (status == AccountStatus.CLOSED) {
      throw new BusinessRuleException(INVALID_ACCOUNT_STATUS_MESSAGE);
    }
  }

  private void hasCreditAccount(final String cifNumber) {
    Optional<List<Account>> accounts = accountRepository.findByCifNumber(cifNumber);
    if (accounts.isPresent()
        && accounts.get().stream().anyMatch(acc -> acc.getAccountStatus() == AccountStatus.OPEN)) {
      throw new BusinessRuleException(PRESENT_ACCOUNT_MESSAGE);
    }
  }

  private void isOfAge(final LocalDate dob) {
    if (Period.between(dob, LocalDate.now()).getYears() < 18) {
      throw new BusinessRuleException(INVALID_AGE_MESSAGE);
    }
  }

  private void isCreditWorthy(
      final ClientDetails client, final OpenCreditAccountRequest openCreditAccountRequest) {
    if (client.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
      throw new BusinessRuleException(EmploymentStatus.UNEMPLOYED.toString());
    }
    if (client.getCredit() == CreditStanding.BAD
        || client.getCredit() == CreditStanding.NEEDS_SUPPORT) {
      throw new BusinessRuleException("Client has poor credit record");
    }
    if (!((percentOf(client.getVerifiedAnnualIncome(), openCreditAccountRequest.getCreditAmount()))
        <= 25)) {
      throw new BusinessRuleException("Credit must be 25% or less of total annual income");
    }
  }

  private void hasIncome(final SourceOfFunds sourceOfFunds) {
    if (sourceOfFunds == SourceOfFunds.NONE) {
      throw new BusinessRuleException("Must have an income");
    }
  }

  private void isClientActive(final ClientDetails client) {
    if (!(client.getClientStatus() == ClientStatus.ACTIVE)) {
      throw new BusinessRuleException(INVALID_CLIENT_STATUS_MESSAGE);
    }
  }

  private void hasBalance(final UUID accountNumber) {
    TransactionsResponse transactionsResponse =
        transactionServiceClient.getAccountTransactions(accountNumber);

    if (transactionsResponse.getBalance() != null
        && transactionsResponse.getBalance().compareTo(BigDecimal.ZERO) > 0) {
      throw new BusinessRuleException("Balance Must Be Zero or Less To Close Account");
    }
    log.info("Balance is Valid for Close");
  }

  private static int percentOf(final BigDecimal base, final BigDecimal value) {
    return (value.divide(base, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)))
        .intValue();
  }
}
