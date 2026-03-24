package com.capsule.corp.domain.persistance;

import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientDetails, UUID> {

  Optional<ClientDetails> findByCifNumber(String cifNumber);

  Optional<ClientDetails> findByIdNumber(String idNumber);
}
