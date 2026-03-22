package com.capsule.corp.domain.persistance;

import com.capsule.corp.infrastructure.http.controller.resources.ClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientDetails, UUID> {

    Optional<ClientDetails> findByCifNumber(String cifNumber);
}
