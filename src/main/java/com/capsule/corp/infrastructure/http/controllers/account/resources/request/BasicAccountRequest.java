package com.capsule.corp.infrastructure.http.controllers.account.resources.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasicAccountRequest {

  @NotNull UUID accountNumber;

  @NotBlank String reason;
}
