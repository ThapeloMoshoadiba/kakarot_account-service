package com.capsule.corp.domain.mapper;

import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.enums.AccountStatus;
import com.capsule.corp.infrastructure.http.controller.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controller.resources.response.AccountSummaryResponse;
import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(componentModel = "spring",
        imports = {UUID.class, LocalDate.class, LocalDate.class, AccountStatus.class})
public interface AccountMapper {

    @Mapping(target = "accountId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(LocalDate.now())")
    @Mapping(target = "cifNumber", source = "client.cifNumber")
    @Mapping(target = "accountNumber", expression = "java(UUID.randomUUID())")
    @Mapping(target = "accountStatus", expression = "java(AccountStatus.OPEN)")
    @Mapping(target = "initialCreditAmount", source = "openCreditAccountRequest.creditAmount")
    Account mapAccountEntity(ClientDetails client, OpenCreditAccountRequest openCreditAccountRequest);

    @Mapping(target = "accountNumber", source = "accountNumber")
    @Mapping(target = "accountStatus", source = "accountStatus")
    @Mapping(target = "initialCreditAmount", source = "initialCreditAmount")
    @Mapping(target = "success", constant = "true")
    AccountSummaryResponse mapAccountSummary(Account account);

    @Mapping(target = "clientDetails", source = "client")
    @Mapping(target = "account", source = "account")
    @Mapping(target = "success", constant = "true")
    AccountDetailedResponse mapAccountDetailed(ClientDetails client, Account account);

}
