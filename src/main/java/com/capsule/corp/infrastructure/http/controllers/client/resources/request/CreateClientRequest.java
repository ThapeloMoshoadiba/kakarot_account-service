package com.capsule.corp.infrastructure.http.controllers.client.resources.request;

import static com.capsule.corp.infrastructure.http.resources.Constants.CELLPHONE_NUMBER_PATTERN;
import static com.capsule.corp.infrastructure.http.resources.Constants.EMAIL_PATTERN;
import static com.capsule.corp.infrastructure.http.resources.Constants.SA_ID_NUMBER_PATTERN;

import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.Gender;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import com.capsule.corp.infrastructure.http.controllers.enums.Title;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {

  @NotNull private Title title;

  @NotNull private Gender gender;

  @NotBlank
  @Pattern(regexp = SA_ID_NUMBER_PATTERN)
  private String idNumber;

  @NotNull private LocalDate dateOfBirth;

  @NotBlank private String firstName;

  private String middleName;

  @NotBlank private String lastName;

  @NotBlank private String address;

  @Nullable
  @Pattern(regexp = CELLPHONE_NUMBER_PATTERN)
  private String cellphoneNumber;

  @Nullable
  @Pattern(regexp = EMAIL_PATTERN)
  private String email;

  private CreditStanding credit;

  @NotNull private EmploymentStatus employmentStatus;

  @NotNull private SourceOfFunds sourceOfFunds;

  @NotNull private BigDecimal verifiedAnnualIncome;
}
