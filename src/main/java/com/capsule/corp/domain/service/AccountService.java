package com.capsule.corp.domain.service;

import com.capsule.corp.common.exception.AccountNotFoundException;
import com.capsule.corp.common.exception.ClientNotFoundException;
import com.capsule.corp.domain.mapper.AccountMapper;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.domain.validation.rules.CloseAccountRules;
import com.capsule.corp.domain.validation.rules.OpenAccountRules;
import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.request.CloseCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controller.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountSummaryResponse;
import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final OpenAccountRules openAccountRules;
    private final CloseAccountRules closeAccountRules;
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    Account account;
    ClientDetails client;
    String failureReason;

    public AccountSummaryResponse openAccount(OpenCreditAccountRequest openCreditAccountRequest) {
            try {
                client = getClient(openCreditAccountRequest.getCifNumber());
                if (openAccountRules.canAccountBeOpened(client, openCreditAccountRequest)) {
                    Account newAccount = accountMapper.mapAccountEntity(client, openCreditAccountRequest);
                    accountRepository.save(newAccount);

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
        try{
            if (accountNumber != null){
                account = getAccount(accountNumber);
                client = getClient(account.getCifNumber());
            } else if (cifNumber != null) {
                client = getClient(cifNumber);
                account = getAccount(cifNumber);
            }
            return accountMapper.mapAccountDetailed(client, account);
        } catch (Exception e) {
            log.error("Unable to retrieve account: ", e);
            failureReason = e.getMessage();
        }

        return AccountDetailedResponse.builder().success(false).reason(failureReason).build();
    }

    public AccountSummaryResponse closeAccount(CloseCreditAccountRequest closeCreditAccountRequest) {
        try {
                account = getAccount(closeCreditAccountRequest.getAccountNumber());

                if (closeAccountRules.canAccountBeClosed(account)) {
                    account.setAccountStatus(AccountStatus.CLOSED);
                    account.setClosedAt(LocalDate.now());
                    account.setReasonForClose(closeCreditAccountRequest.getReasonForClose());
                    accountRepository.save(account);

                    log.info("Credit Account [{}] successfully closed", account.getAccountNumber());
                    return accountMapper.mapAccountSummary(account);
                }
            } catch (Exception e) {
                log.error("Unable to close account [{}]", closeCreditAccountRequest.getAccountNumber(), e);
                failureReason = e.getMessage();
            }

        return AccountSummaryResponse.builder().success(false).reason(failureReason).build();
    }

    private ClientDetails getClient(String cifNumber){
        Optional<ClientDetails> client = clientRepository.findByCifNumber(cifNumber);

        if (client.isEmpty()){
            throw new ClientNotFoundException("Client not found");
        }
        return client.get();
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
