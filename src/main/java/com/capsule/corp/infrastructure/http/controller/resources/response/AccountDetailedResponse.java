package com.capsule.corp.infrastructure.http.controller.resources.response;

import com.capsule.corp.infrastructure.http.controller.resources.Account;
import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
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
