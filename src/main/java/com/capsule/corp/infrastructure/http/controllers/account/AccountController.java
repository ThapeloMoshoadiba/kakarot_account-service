package com.capsule.corp.infrastructure.http.controllers.account;

import com.capsule.corp.domain.service.AccountService;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.BasicAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.request.OpenCreditAccountRequest;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.account.resources.response.AccountSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-service/account")
// @SecurityRequirement(name = "Bearer Authentication (JWT)")
@Tag(name = "Account Service", description = "Handles Client Credit Accounts")
public class AccountController {

  // we need a JWT token to ensure that the person making calls on these endpoints is allowed to do
  // so (e.g., an employee)

  private final AccountService accountService;

  @Operation(summary = "Open New Credit Account")
  @PutMapping
  public ResponseEntity<AccountSummaryResponse> openAccount(
      @Valid @RequestBody final OpenCreditAccountRequest openCreditAccountRequest) {
    return accountService.openAccount(openCreditAccountRequest);
  }

  @Operation(summary = "Retrieve Account Details")
  @GetMapping
  public ResponseEntity<AccountDetailedResponse> getAccount(
      @RequestParam(value = "accountNumber", required = false) final UUID accountNumber,
      @RequestParam(value = "cifNumber", required = false) final String cifNumber) {
    return accountService.getAccount(accountNumber, cifNumber);
  }

  @Operation(summary = "Block Account")
  @PutMapping("/block")
  public ResponseEntity<AccountSummaryResponse> blockAccount(
      @Valid @RequestBody final BasicAccountRequest accountRequest) {
    return accountService.blockAccount(accountRequest);
  }

  @Operation(summary = "Unblock Account")
  @PutMapping("/unblock")
  public ResponseEntity<AccountSummaryResponse> unblockAccount(
      @Valid @RequestBody final BasicAccountRequest accountRequest) {
    return accountService.unblockAccount(accountRequest);
  }

  @Operation(summary = "Close Account")
  @PutMapping("/close")
  public ResponseEntity<AccountSummaryResponse> closeAccount(
      @Valid @RequestBody final BasicAccountRequest accountRequest) {
    return accountService.closeAccount(accountRequest);
  }
}
