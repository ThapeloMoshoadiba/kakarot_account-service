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
  private UUID accountId;

  private LocalDateTime createdAt;
  private String cifNumber;
  private UUID accountNumber;

  @Enumerated(EnumType.STRING)
  private AccountStatus accountStatus;

  private BigDecimal initialCreditAmount;

  private LocalDateTime blockedAt;
  private String reasonForBlock;

  private LocalDateTime unblockedAt;
  private String reasonForUnblock;

  private LocalDateTime closedAt;
  private String reasonForClose;
}
