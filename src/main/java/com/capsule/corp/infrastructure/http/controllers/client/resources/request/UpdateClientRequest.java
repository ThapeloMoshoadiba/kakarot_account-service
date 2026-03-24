package com.capsule.corp.infrastructure.http.controllers.client.resources.request;

import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientRequest {

  String cifNumber;
  String lastName;

  String address;
  String cellphoneNumber;
  String email;

  CreditStanding credit;
  EmploymentStatus employmentStatus;
  SourceOfFunds sourceOfFunds;
  BigDecimal verifiedAnnualIncome;

  ClientStatus clientStatus;
}
