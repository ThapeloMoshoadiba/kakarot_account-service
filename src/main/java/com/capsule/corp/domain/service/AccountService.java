package com.capsule.corp.domain.service;

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
import org.springframework.http.HttpStatus;
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
  String failureReason;
  List<Account> accountsList = new ArrayList<>();

  public ResponseEntity<AccountSummaryResponse> openAccount(
      OpenCreditAccountRequest openCreditAccountRequest) {
    try {
      client =
          clientService
              .getClient(null, openCreditAccountRequest.getCifNumber())
              .getBody()
              .getClientDetails();
      accountRules.canAccountBeOpened(client, openCreditAccountRequest);

      Account newAccount = accountMapper.mapAccountEntity(client, openCreditAccountRequest);
      accountRepository.save(newAccount);

      account = getAccount(newAccount.getAccountNumber());
      if (account.getAccountStatus() == AccountStatus.OPEN) {
        log.info(
            "Credit Account successfully opened for Client [{}]",
            openCreditAccountRequest.getCifNumber());

        transactionServiceClient.openAccountTransaction(
            accountMapper.mapAccountToTransaction(
                account, openCreditAccountRequest.getCreditAmount()));

        return ResponseEntity.ok(accountMapper.mapAccountSummary(newAccount));
      }

    } catch (AccountNotFoundException e) {
      failureReason = e.getMessage();
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error(
          "Unable to open account for Client [{}]: [{}]", client.getCifNumber(), failureReason);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
  }

  public ResponseEntity<AccountDetailedResponse> getAccount(UUID accountNumber, String cifNumber) {
    try {
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
    } catch (AccountNotFoundException e) {
      failureReason = e.getMessage();
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountDetailedResponse.builder().success(false).reason(failureReason).build());
    } catch (Exception e) {
      log.error("Unable to retrieve account: ", e);
      failureReason = e.getMessage();
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AccountDetailedResponse.builder().success(false).reason(failureReason).build());
  }

  public ResponseEntity<AccountSummaryResponse> blockAccount(BasicAccountRequest accountRequest) {
    try {
      account = getAccount(accountRequest.getAccountNumber());
      accountRules.canAccountBeBlocked(account);

      account.setAccountStatus(AccountStatus.BLOCKED);
      account.setBlockedAt(LocalDateTime.now());
      account.setReasonForBlock(accountRequest.getReason());
      accountRepository.save(account);

      if (getAccount(accountRequest.getAccountNumber()).getAccountStatus()
          == AccountStatus.BLOCKED) {
        log.info("Credit Account [{}] successfully blocked", account.getAccountNumber());
        return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
      }
    } catch (AccountNotFoundException e) {
      failureReason = e.getMessage();
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
    } catch (Exception e) {
      log.error("Unable to block account [{}]", accountRequest.getAccountNumber(), e);
      failureReason = e.getMessage();
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
  }

  public ResponseEntity<AccountSummaryResponse> unblockAccount(BasicAccountRequest accountRequest) {
    try {
      account = getAccount(accountRequest.getAccountNumber());
      accountRules.canAccountBeUnblocked(account);

      account.setAccountStatus(AccountStatus.OPEN);
      account.setUnblockedAt(LocalDateTime.now());
      account.setReasonForUnblock(accountRequest.getReason());
      accountRepository.save(account);

      if (getAccount(accountRequest.getAccountNumber()).getAccountStatus() == AccountStatus.OPEN) {
        log.info("Credit Account [{}] successfully unblocked", account.getAccountNumber());
        return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
      }
    } catch (AccountNotFoundException e) {
      failureReason = e.getMessage();
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error(
          "Unable to unblock account [{}]: [{}]", accountRequest.getAccountNumber(), failureReason);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
  }

  public ResponseEntity<AccountSummaryResponse> closeAccount(BasicAccountRequest accountRequest) {
    try {
      account = getAccount(accountRequest.getAccountNumber());
      accountRules.canAccountBeClosed(account);

      account.setAccountStatus(AccountStatus.CLOSED);
      account.setClosedAt(LocalDateTime.now());
      account.setReasonForClose(accountRequest.getReason());
      accountRepository.save(account);

      if (getAccount(accountRequest.getAccountNumber()).getAccountStatus()
          == AccountStatus.CLOSED) {
        return ResponseEntity.ok(accountMapper.mapAccountSummary(account));
      }
    } catch (AccountNotFoundException e) {
      failureReason = e.getMessage();
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
          .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
    } catch (Exception e) {
      failureReason = e.getMessage();
      log.error(
          "Unable to close account [{}]: [{}]", accountRequest.getAccountNumber(), failureReason);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(AccountSummaryResponse.builder().success(false).reason(failureReason).build());
  }

  private Account getAccount(UUID accountNumber) {
    Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
    if (account.isEmpty()) {
      throw new AccountNotFoundException("Account [{}] not found", accountNumber.toString());
    }
    return account.get();
  }

  private List<Account> getAccounts(String cifNumber) {
    Optional<List<Account>> accounts = accountRepository.findByCifNumber(cifNumber);
    if (accounts.isEmpty()) {
      throw new AccountNotFoundException("No accounts found for CIF number [{}]", cifNumber);
    }
    return accounts.get();
  }
}
