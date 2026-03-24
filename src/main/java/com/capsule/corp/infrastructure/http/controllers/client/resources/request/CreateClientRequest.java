package com.capsule.corp.infrastructure.http.controllers.client.resources.request;

import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.Gender;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import com.capsule.corp.infrastructure.http.controllers.enums.Title;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateClientRequest {

  Title title;
  Gender gender;

  String idNumber;
  LocalDate dateOfBirth;
  String firstName;
  String middleName;
  String lastName;

  String address;
  String cellphoneNumber;
  String email;

  CreditStanding credit;
  EmploymentStatus employmentStatus;
  SourceOfFunds sourceOfFunds;
  BigDecimal verifiedAnnualIncome;
}
