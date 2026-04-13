package com.capsule.corp.infrastructure.http.controllers.account.resources.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenCreditAccountRequest {

  @NotBlank String cifNumber;

  @NotNull @Positive BigDecimal creditAmount;
}
