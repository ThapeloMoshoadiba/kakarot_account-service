package com.capsule.corp.infrastructure.http.controllers.account.resources;

import com.capsule.corp.infrastructure.http.controllers.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

  @Id
  @Column(name = "account_id", nullable = false, updatable = false)
  UUID accountId;

  LocalDateTime createdAt;
  String cifNumber;
  UUID accountNumber;

  @Enumerated(EnumType.STRING)
  AccountStatus accountStatus;

  BigDecimal initialCreditAmount;
  LocalDateTime closedAt;
  String reasonForClose;
}
