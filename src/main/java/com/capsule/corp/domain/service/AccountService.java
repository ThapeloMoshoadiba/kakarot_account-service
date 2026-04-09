package com.capsule.corp.domain.service;

import static com.capsule.corp.infrastructure.http.resources.Constants.ACCOUNT_NOT_FOUND_MESSAGE;

import com.capsule.corp.common.exception.AccountNotFoundException;
import com.capsule.corp.domain.mapper.AccountMapper;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.domain.validation.rules.AccountRules;
import com.capsule.corp.infrastructure.http.clients.transactions.TransactionServiceClient;
import com.capsule.corp.infrastructure.http.controllers.account.resources.Account;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.BasicAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRules accountRules;
  private final AccountMapper accountMapper;
  private final ClientService clientService;
  private final AccountRepository accountRepository;
  private final TransactionServiceClient transactionServiceClient;

  Account account;
  ClientDetails client;
  List<Account> accountsList = new ArrayList<>();

  public ResponseEntity<AccountSummaryResponse> openAccount(
      OpenCreditAccountRequest openCreditAccountRequest) {
    client =
        clientService
            .getClient(null, openCreditAccountRequest.getCifNumber())
            .getBody()
            .getClientDetails();
    accountRules.canAccountBeOpened(client, openCreditAccountRequest);

    Account newAccount = accountMapper.mapAccountEntity(client, openCreditAccountRequest);
    accountRepository.save(newAccount);

    transactionServiceClient.openAccountTransaction(
        accountMapper.mapAccountToTransaction(account, openCreditAccountRequest.getCreditAmount()));

    return ResponseEntity.ok(accountMapper.mapAccountSummary(newAccount));
  }

  public ResponseEntity<AccountDetailedResponse> getAccount(UUID accountNumber, String cifNumber) {
    if (accountNumber != null) {
      accountsList.add(getAccount(accountNumber));
      client =
          clientService
              .getClient(null, accountsList.getFirst().getCifNumber())
              .getBody()
              .getClientDetails();
    } else if (cifNumber != null) {
      accountsList = getAccounts(cifNumber);
      client = clientService.getClient(null, cifNumber).getBody().getClientDetails();
    }
    return ResponseEntity.ok(accountMapper.mapAccountDetailed(client, accountsList));
  }

  public ResponseEntity<AccountSummaryResponse> blockAccount(BasicAccountRequest accountRequest) {
    account = getAccount(accountRequest.getAccountNumber());
    accountRules.canAccountBeBlocked(account);

    account.setAccountStatus(AccountStatus.BLOCKED);
    account.setBlockedAt(LocalDateTime.now());
    account.setReasonForBlock(accountRequest.getReason());
    accountRepository.save(account);

    return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
  }

  public ResponseEntity<AccountSummaryResponse> unblockAccount(BasicAccountRequest accountRequest) {
    account = getAccount(accountRequest.getAccountNumber());
    accountRules.canAccountBeUnblocked(account);

    account.setAccountStatus(AccountStatus.OPEN);
    account.setUnblockedAt(LocalDateTime.now());
    account.setReasonForUnblock(accountRequest.getReason());
    accountRepository.save(account);

    return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
  }

  public ResponseEntity<AccountSummaryResponse> closeAccount(BasicAccountRequest accountRequest) {
    account = getAccount(accountRequest.getAccountNumber());
    accountRules.canAccountBeClosed(account);

    account.setAccountStatus(AccountStatus.CLOSED);
    account.setClosedAt(LocalDateTime.now());
    account.setReasonForClose(accountRequest.getReason());
    accountRepository.save(account);

    return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
  }

  private Account getAccount(UUID accountNumber) {
    Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
    if (account.isEmpty()) {
      throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE);
    }
    return account.get();
  }

  private List<Account> getAccounts(String cifNumber) {
    Optional<List<Account>> accounts = accountRepository.findByCifNumber(cifNumber);
    if (accounts.isEmpty()) {
      throw new AccountNotFoundException(ACCOUNT_NOT_FOUND_MESSAGE);
    }
    return accounts.get();
  }
}
