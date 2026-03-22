package com.capsule.corp.infrastructure.http.controller.resources.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenCreditAccountRequest {

    String cifNumber;
    BigDecimal creditAmount;

}
