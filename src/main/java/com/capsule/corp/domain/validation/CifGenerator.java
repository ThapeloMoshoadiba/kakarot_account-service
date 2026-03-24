package com.capsule.corp.domain.validation;

import com.capsule.corp.domain.persistance.ClientRepository;
import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CifGenerator {

  private final ClientRepository clientRepository;

  public String generateCifNumber() {
    Optional<ClientDetails> client;
    String cifNumber;

    do {
      cifNumber = generate();
      client = clientRepository.findByCifNumber(cifNumber);

      if (client.isPresent()) {
        log.info("Client already exists for cif number: " + cifNumber);
      }
    } while (client.isPresent());

    return cifNumber;
  }

  private String generate() {
    StringBuilder sb = new StringBuilder();

    // First digit (1–9) → prevents leading zero
    sb.append(ThreadLocalRandom.current().nextInt(1, 10));

    // Remaining 16 digits (0–9)
    for (int i = 0; i < 16; i++) {
      sb.append(ThreadLocalRandom.current().nextInt(10));
    }

    return sb.toString();
  }
}
