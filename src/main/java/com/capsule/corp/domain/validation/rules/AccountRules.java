package com.capsule.corp.domain.validation.rules;

import static com.capsule.corp.infrastructure.http.resources.Constants.ACTIVE_CLIENT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.BLOCKED_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.CLIENT_NOT_FOUND_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.CLOSED_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NOT_ACTIVE_CLIENT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NOT_BLOCKED_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NOT_CLOSED_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NOT_OPEN_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.NOT_PRESENT_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.OF_AGE_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.OPEN_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.PRESENT_ACCOUNT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.PRESENT_CLIENT_MESSAGE;
import static com.capsule.corp.infrastructure.http.resources.Constants.UNDER_AGE_MESSAGE;

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
      ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest) {
    log.info("Running Open Account Rules for client [{}]", openCreditAccountRequest.getCifNumber());
    hasCreditAccount(openCreditAccountRequest.getCifNumber());
    isOfAge(client.getDateOfBirth());
    hasIncome(client.getSourceOfFunds());
    isCreditWorthy(client, openCreditAccountRequest);
    isClientActive(client);

    log.info("Open Account Rules Passed for client [{}]", openCreditAccountRequest.getCifNumber());
  }

  public void canAccountBeClosed(Account account) {
    log.info("Running Close Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    notClosed(account.getAccountStatus());
    hasBalance(account.getAccountNumber());
    notOpen(account.getAccountStatus());

    log.info("Close Account Rules Passed for account [{}]", account.getAccountNumber());
  }

  public void canAccountBeBlocked(Account account) {
    log.info("Running Block Account Rules for account [{}]", account.getAccountNumber());
    hasClient(account.getCifNumber());
    notBlocked(account.getAccountStatus());
    notOpen(account.getAccountStatus());

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
      throw new BusinessRuleException(CLIENT_NOT_FOUND_MESSAGE);
    }
    log.info(PRESENT_CLIENT_MESSAGE);
  }

  private void notOpen(AccountStatus status) {
    if (!(status == AccountStatus.OPEN)) {
      throw new BusinessRuleException(NOT_OPEN_ACCOUNT_MESSAGE);
    }
    log.info(OPEN_ACCOUNT_MESSAGE);
  }

  private void notBlocked(AccountStatus status) {
    if (status == AccountStatus.BLOCKED) {
      throw new BusinessRuleException(BLOCKED_ACCOUNT_MESSAGE);
    }
    log.info(NOT_BLOCKED_ACCOUNT_MESSAGE);
  }

  private void isBlocked(AccountStatus status) {
    if (!(status == AccountStatus.BLOCKED)) {
      throw new BusinessRuleException(NOT_BLOCKED_ACCOUNT_MESSAGE);
    }
    log.info(BLOCKED_ACCOUNT_MESSAGE);
  }

  private void notClosed(AccountStatus status) {
    if (status == AccountStatus.CLOSED) {
      throw new BusinessRuleException(CLOSED_ACCOUNT_MESSAGE);
    }
    log.info(NOT_CLOSED_ACCOUNT_MESSAGE);
  }

  private void hasCreditAccount(String cifNumber) {
    Optional<List<Account>> accounts = accountRepository.findByCifNumber(cifNumber);
    if (accounts.isPresent()
        && accounts.get().stream().anyMatch(acc -> acc.getAccountStatus() == AccountStatus.OPEN)) {
      throw new BusinessRuleException(PRESENT_ACCOUNT_MESSAGE);
    }
    log.info(NOT_PRESENT_ACCOUNT_MESSAGE);
  }

  private void isOfAge(LocalDate dob) {
    if (Period.between(dob, LocalDate.now()).getYears() < 18) {
      throw new BusinessRuleException(UNDER_AGE_MESSAGE);
    }
    log.info(OF_AGE_MESSAGE);
  }

  private void isCreditWorthy(
      ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest) {
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
      throw new BusinessRuleException(NOT_ACTIVE_CLIENT_MESSAGE);
    }
    log.info(ACTIVE_CLIENT_MESSAGE);
  }

  private void hasBalance(UUID accountNumber) {
    TransactionsResponse transactionsResponse =
        transactionServiceClient.getAccountTransactions(accountNumber);

    if (transactionsResponse.getBalance() != null
        && transactionsResponse.getBalance().compareTo(BigDecimal.ZERO) > 0) {
      throw new BusinessRuleException("Balance Must Be Zero or Less To Close Account");
    }
    log.info("Balance is Valid for Close");
  }

  private static int percentOf(BigDecimal base, BigDecimal value) {
    return (value.divide(base, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)))
        .intValue();
  }
}
