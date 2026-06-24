package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.domain.validators.PrimeNumberValidator;
import com.challenge.token_validation_service.shared.constants.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SeedRule implements ValidationRule {

  @Override
  public ValidationResult validate(ClaimData claims) {

    log.debug("Executing validation rule. rule=SeedRule");

    String seed = claims.getSeed();

    if (seed == null || seed.isBlank()) {

      log.warn("Validation failed. rule=SeedRule, reason=Seed is empty");

      return ValidationResult.failure(ErrorMessages.SEED_EMPTY);
    }

    if (!PrimeNumberValidator.isPrime(seed)) {

      log.warn("Validation failed. rule=SeedRule, reason=Seed is not prime");

      return ValidationResult.failure(ErrorMessages.SEED_NOT_PRIME);
    }

    log.debug("Validation rule passed. rule=SeedRule");

    return ValidationResult.success();
  }

  @Override
  public int getOrder() {
    return 4;
  }
}
