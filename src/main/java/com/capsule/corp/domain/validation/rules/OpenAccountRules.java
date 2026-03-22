package com.capsule.corp.domain.validation.rules;

import com.capsule.corp.common.exception.BusinessRuleException;
import com.capsule.corp.domain.persistance.AccountRepository;
import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controller.resources.enums.SourceOfFunds;
import com.capsule.corp.infrastructure.http.controller.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controller.resources.enums.EmploymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAccountRules {
    private final AccountRepository accountRepository;

    public boolean canAccountBeOpened(ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest){
        log.info("Running Open Account Rules for client [{}]", openCreditAccountRequest.getCifNumber());
        hasCreditAccount(openCreditAccountRequest.getCifNumber());
        isOfAge(client.getDateOfBirth());
        hasIncome(client.getSourceOfFunds());
        isCreditWorthy(client, openCreditAccountRequest);

        log.info("Open Account Rules Passed for client [{}]", openCreditAccountRequest.getCifNumber());
        return true;
    }

    private void hasCreditAccount(String cifNumber) {
        Optional<Account> account = accountRepository.findByCifNumber(cifNumber);
        if (account.isPresent() && account.get().getAccountStatus() == AccountStatus.OPEN) {
            throw new BusinessRuleException("Open Credit Account already exists for Client");
        }
        log.info("Client does not have an Open Credit Account");
    }

    private void isOfAge(LocalDate dob){
        if (Period.between(dob, LocalDate.now()).getYears() < 18){
            throw new BusinessRuleException("Must Be 18 or older");
        }
        log.info("Client is of age");
    }

    private void isCreditWorthy(ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest){
        if (client.getEmploymentStatus() == EmploymentStatus.UNEMPLOYED){
            throw new BusinessRuleException("Must be employed");
        }
        if (client.getCredit() == CreditStanding.BAD || client.getCredit() == CreditStanding.NEEDS_SUPPORT){
            throw new BusinessRuleException("Client has poor credit record");
        }
        if (!((percentOf(client.getVerifiedAnnualIncome(), openCreditAccountRequest.getCreditAmount())) <= 25)){
            throw new BusinessRuleException("Credit must be 25% or less of total annual income");
        }
        log.info("Client is credit-worthy");
    }

    private void hasIncome(SourceOfFunds sourceOfFunds){
        if (sourceOfFunds == SourceOfFunds.NONE){
            throw new BusinessRuleException("Must have an income");
        }
        log.info("Client has income");
    }

    private static int percentOf(BigDecimal base, BigDecimal value) {
        return (value
                .divide(base, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))).intValue();
    }

}
