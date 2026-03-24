package com.capsule.corp.domain.service;

import com.capsule.corp.common.exception.AccountNotFoundException;
import com.capsule.corp.domain.mapper.AccountMapper;
import com.capsule.corp.domain.mapper.ClientMapper;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.domain.validation.rules.CloseAccountRules;
import com.capsule.corp.domain.validation.rules.OpenAccountRules;
import com.capsule.corp.infrastructure.http.clients.transactions.TransactionServiceClient;
import com.capsule.corp.infrastructure.http.controllers.account.resources.Account;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.CloseCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final OpenAccountRules openAccountRules;
  private final CloseAccountRules closeAccountRules;
  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final ClientMapper clientMapper;
  private final TransactionServiceClient transactionServiceClient;
  private final ClientService clientService;

  Account account;
  ClientDetails client;
  String failureReason;

  public AccountSummaryResponse openAccount(OpenCreditAccountRequest openCreditAccountRequest) {
    try {
      client =
          clientService.getClient(null, openCreditAccountRequest.getCifNumber()).getClientDetails();
      if (openAccountRules.canAccountBeOpened(client, openCreditAccountRequest)) {
        Account newAccount = accountMapper.mapAccountEntity(client, openCreditAccountRequest);
        accountRepository.save(newAccount);

        transactionServiceClient.openAccountTransaction(
            accountMapper.mapAccountToTransaction(
                account, openCreditAccountRequest.getCreditAmount()));

        log.info("Credit Account successfully opened for Client [{}]", client.getCifNumber());
        return accountMapper.mapAccountSummary(newAccount);
      }
    } catch (Exception e) {
      log.error("Unable to open account for Client [{}]", client.getCifNumber(), e);
      failureReason = e.getMessage();
    }
    return AccountSummaryResponse.builder().success(false).reason(failureReason).build();
  }

  public AccountDetailedResponse getAccount(UUID accountNumber, String cifNumber) {
    try {
      if (accountNumber != null) {
        account = getAccount(accountNumber);
        client = clientService.getClient(null, account.getCifNumber()).getClientDetails();
      } else if (cifNumber != null) {
        client = clientService.getClient(null, cifNumber).getClientDetails();
        account = getAccount(cifNumber);
      }
      return accountMapper.mapAccountDetailed(client, account);
    } catch (Exception e) {
      log.error("Unable to retrieve account: ", e);
      failureReason = e.getMessage();
    }

    return AccountDetailedResponse.builder().success(false).reason(failureReason).build();
  }

  public void updateAccount() {
    // Look into which parameters in Account can be updated
  }

  public AccountSummaryResponse closeAccount(CloseCreditAccountRequest closeCreditAccountRequest) {
    try {
      account = getAccount(closeCreditAccountRequest.getAccountNumber());

      if (closeAccountRules.canAccountBeClosed(account)) {
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        account.setReasonForClose(closeCreditAccountRequest.getReasonForClose());
        accountRepository.save(account);

        clientService.blockClient(
            clientMapper.mapRemoveClientRequest(
                account.getCifNumber(), closeCreditAccountRequest.getReasonForClose()));

        log.info("Credit Account [{}] successfully closed", account.getAccountNumber());
        return accountMapper.mapAccountSummary(account);
      }
    } catch (Exception e) {
      log.error("Unable to close account [{}]", closeCreditAccountRequest.getAccountNumber(), e);
      failureReason = e.getMessage();
    }
    return AccountSummaryResponse.builder().success(false).reason(failureReason).build();
  }

  public void closeAccountWithBalance() {
    // This should also include a payment transaction that sets the balance to zero
  }

  private Account getAccount(UUID accountNumber) {
    Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
    if (account.isEmpty()) {
      throw new AccountNotFoundException("Account not found");
    }
    return account.get();
  }

  private Account getAccount(String cifNumber) {
    Optional<Account> account = accountRepository.findByCifNumber(cifNumber);
    if (account.isEmpty()) {
      throw new AccountNotFoundException("Account not found");
    }
    return account.get();
  }
}
