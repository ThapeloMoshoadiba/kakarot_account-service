package com.capsule.corp.infrastructure.http.controllers.client.resources.request;

import static com.capsule.corp.infrastructure.http.resources.Constants.CELLPHONE_NUMBER_PATTERN;
import static com.capsule.corp.infrastructure.http.resources.Constants.EMAIL_PATTERN;

import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

  @NotBlank String cifNumber;

  String lastName;

  String address;

  @Nullable
  @Pattern(regexp = CELLPHONE_NUMBER_PATTERN)
  String cellphoneNumber;

  @Nullable
  @Pattern(regexp = EMAIL_PATTERN)
  String email;

  CreditStanding credit;
  EmploymentStatus employmentStatus;
  SourceOfFunds sourceOfFunds;
  BigDecimal verifiedAnnualIncome;
}
