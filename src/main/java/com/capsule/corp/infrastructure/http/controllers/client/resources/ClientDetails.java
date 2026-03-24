package com.capsule.corp.infrastructure.http.controllers.client.resources;

import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controllers.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controllers.enums.Gender;
import com.capsule.corp.infrastructure.http.controllers.enums.SourceOfFunds;
import com.capsule.corp.infrastructure.http.controllers.enums.Title;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class ClientDetails {
  @Id
  @Column(name = "client_id", nullable = false, updatable = false)
  UUID clientId;

  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  String cifNumber;

  @Enumerated(EnumType.STRING)
  Title title;

  String firstName;
  String middleName;
  String lastName;
  String idNumber;

  @Enumerated(EnumType.STRING)
  Gender gender;

  LocalDate dateOfBirth;
  String address;
  String cellphoneNumber;
  String email;

  @Enumerated(EnumType.STRING)
  CreditStanding credit;

  @Enumerated(EnumType.STRING)
  EmploymentStatus employmentStatus;

  @Enumerated(EnumType.STRING)
  SourceOfFunds sourceOfFunds;

  BigDecimal verifiedAnnualIncome;

  @Enumerated(EnumType.STRING)
  ClientStatus clientStatus;

  LocalDateTime blockedAt;
  String reasonForBlock;

  LocalDateTime unblockedAt;
  String reasonForUnblock;
}
