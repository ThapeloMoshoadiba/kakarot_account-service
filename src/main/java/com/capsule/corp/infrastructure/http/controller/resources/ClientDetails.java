package com.capsule.corp.infrastructure.http.controller.resources;

import com.capsule.corp.infrastructure.http.controller.resources.enums.CreditStanding;
import com.capsule.corp.infrastructure.http.controller.resources.enums.EmploymentStatus;
import com.capsule.corp.infrastructure.http.controller.resources.enums.SourceOfFunds;
import com.capsule.corp.infrastructure.http.controller.resources.enums.Title;
import com.capsule.corp.infrastructure.http.controller.resources.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
public class ClientDetails {
    @Id
    @Column(name = "client_id", nullable = false, updatable = false)
    UUID clientId;

    LocalDate createdAt;
    LocalDate updatedAt;
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
}
