package com.capsule.corp.infrastructure.http.controllers.account.resources.response;

import com.capsule.corp.infrastructure.http.controllers.account.resources.Account;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDetailedResponse {
  ClientDetails clientDetails;
  Account account;
  boolean success;
  String reason;
}
