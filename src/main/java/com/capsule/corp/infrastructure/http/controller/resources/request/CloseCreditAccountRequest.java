package com.capsule.corp.infrastructure.http.controller.resources.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseCreditAccountRequest {

    UUID accountNumber;
    String reasonForClose;

}
