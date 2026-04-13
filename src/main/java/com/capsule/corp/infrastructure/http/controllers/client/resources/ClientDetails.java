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
  private UUID clientId;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String cifNumber;

  @Enumerated(EnumType.STRING)
  private Title title;

  private String firstName;
  private String middleName;
  private String lastName;
  private String idNumber;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private LocalDate dateOfBirth;
  private String address;
  private String cellphoneNumber;
  private String email;

  @Enumerated(EnumType.STRING)
  private CreditStanding credit;

  @Enumerated(EnumType.STRING)
  private EmploymentStatus employmentStatus;

  @Enumerated(EnumType.STRING)
  private SourceOfFunds sourceOfFunds;

  private BigDecimal verifiedAnnualIncome;

  @Enumerated(EnumType.STRING)
  private ClientStatus clientStatus;

  private LocalDateTime blockedAt;
  private String reasonForBlock;

  private LocalDateTime unblockedAt;
  private String reasonForUnblock;
}
