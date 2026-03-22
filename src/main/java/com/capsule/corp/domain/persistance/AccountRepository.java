package com.capsule.corp.domain.persistance;

import com.capsule.corp.infrastructure.http.controller.resources.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByCifNumber(String cifNumber);
    Optional<Account> findByAccountNumber(UUID accountNumber);
}
