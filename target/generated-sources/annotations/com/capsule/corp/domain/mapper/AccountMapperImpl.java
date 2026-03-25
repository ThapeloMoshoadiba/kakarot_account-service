package com.capsule.corp.domain.mapper;

import com.capsule.corp.infrastructure.http.clients.transactions.resources.TransactionRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.Account;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-25T12:30:29+0200",
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
            account.blockedAt( client.getBlockedAt() );
            account.reasonForBlock( client.getReasonForBlock() );
            account.unblockedAt( client.getUnblockedAt() );
            account.reasonForUnblock( client.getReasonForUnblock() );
        }
        if ( openCreditAccountRequest != null ) {
            account.initialCreditAmount( openCreditAccountRequest.getCreditAmount() );
        }
        account.accountId( UUID.randomUUID() );
        account.createdAt( LocalDateTime.now() );
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
    public AccountDetailedResponse mapAccountDetailed(ClientDetails client, List<Account> accounts) {
        if ( client == null && accounts == null ) {
            return null;
        }

        AccountDetailedResponse.AccountDetailedResponseBuilder accountDetailedResponse = AccountDetailedResponse.builder();

        accountDetailedResponse.clientDetails( client );
        List<Account> list = accounts;
        if ( list != null ) {
            accountDetailedResponse.accounts( new ArrayList<Account>( list ) );
        }
        accountDetailedResponse.success( true );

        return accountDetailedResponse.build();
    }

    @Override
    public TransactionRequest mapAccountToTransaction(Account account, BigDecimal amount) {
        if ( account == null && amount == null ) {
            return null;
        }

        TransactionRequest.TransactionRequestBuilder transactionRequest = TransactionRequest.builder();

        if ( account != null ) {
            transactionRequest.accountNumber( account.getAccountNumber() );
        }
        transactionRequest.amount( amount );

        return transactionRequest.build();
    }
}
