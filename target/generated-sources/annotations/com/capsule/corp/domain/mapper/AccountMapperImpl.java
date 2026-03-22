package com.capsule.corp.domain.mapper;

import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import com.capsule.corp.infrastructure.http.controller.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountSummaryResponse;
import java.time.LocalDate;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T20:40:29+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account mapAccountEntity(ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest) {
        if ( client == null && openCreditAccountRequest == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        if ( client != null ) {
            account.cifNumber( client.getCifNumber() );
        }
        if ( openCreditAccountRequest != null ) {
            account.initialCreditAmount( openCreditAccountRequest.getCreditAmount() );
        }
        account.accountId( UUID.randomUUID() );
        account.createdAt( LocalDate.now() );
        account.accountNumber( UUID.randomUUID() );
        account.accountStatus( AccountStatus.OPEN );

        return account.build();
    }

    @Override
    public AccountSummaryResponse mapAccountSummary(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountSummaryResponse.AccountSummaryResponseBuilder accountSummaryResponse = AccountSummaryResponse.builder();

        accountSummaryResponse.accountNumber( account.getAccountNumber() );
        accountSummaryResponse.accountStatus( account.getAccountStatus() );
        accountSummaryResponse.initialCreditAmount( account.getInitialCreditAmount() );

        accountSummaryResponse.success( true );

        return accountSummaryResponse.build();
    }

    @Override
    public AccountDetailedResponse mapAccountDetailed(ClientDetails client, Account account) {
        if ( client == null && account == null ) {
            return null;
        }

        AccountDetailedResponse.AccountDetailedResponseBuilder accountDetailedResponse = AccountDetailedResponse.builder();

        accountDetailedResponse.clientDetails( client );
        accountDetailedResponse.account( account );
        accountDetailedResponse.success( true );

        return accountDetailedResponse.build();
    }
}
