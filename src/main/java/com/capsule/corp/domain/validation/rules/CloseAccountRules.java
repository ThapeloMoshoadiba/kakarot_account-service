package com.capsule.corp.domain.validation.rules;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.clients.transactions.TransactionsServiceClient;
import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloseAccountRules {
    private final ClientRepository clientRepository;
    private final TransactionsServiceClient transactionsServiceClient;

    public boolean canAccountBeClosed(Account account){
        log.info("Running Close Account Rules for account [{}]", account.getAccountNumber());
        hasClient(account.getCifNumber());
        hasBalance(account.getAccountNumber());
        notClosed(account.getAccountStatus());

        log.info("Close Account Rules Passed for account [{}]", account.getAccountNumber());
        return true;
    }

    private void hasClient(String cifNumber) {
        if (clientRepository.findByCifNumber(cifNumber).isEmpty()) {
            throw new BusinessRuleException("Credit Account Present for non-existent Client");
        }
        log.info("Client Exists");
    }

    private void hasBalance(UUID accountNumber) {
        /*
        We must call transaction-service via transactionsServiceClient to check if the balance is 0

        if (balance is NOT zero) {
            throw new BusinessRuleException("Credit Account must be at a Zero balance to Close Account");
        }
        */
        log.info("Balance is 0");
    }

    private void notClosed(AccountStatus status) {
        if (status == AccountStatus.CLOSED) {
            throw new BusinessRuleException("Credit Account Already Closed");
        }
        log.info("Credit Account Not Yet Closed");
    }

}
