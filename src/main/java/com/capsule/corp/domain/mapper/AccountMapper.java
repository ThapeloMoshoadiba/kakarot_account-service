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
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    imports = {UUID.class, LocalDate.class, LocalDateTime.class, AccountStatus.class})
public interface AccountMapper {

  @Mapping(target = "accountId", expression = "java(UUID.randomUUID())")
  @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
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

  @Mapping(target = "transactionId", expression = "java(UUID.randomUUID())")
  @Mapping(target = "accountNumber", expression = "java(account.getAccountNumber().toString())")
  @Mapping(target = "amount", constant = "amount")
  TransactionRequest mapAccountToTransaction(Account account, BigDecimal amount);
}
