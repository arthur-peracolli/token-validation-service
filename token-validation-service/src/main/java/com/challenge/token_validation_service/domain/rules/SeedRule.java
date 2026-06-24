package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.shared.constants.ErrorMessages;
import com.challenge.token_validation_service.shared.utils.PrimeNumberValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeedRule implements ValidationRule {

    @Override
    public ValidationResult validate(ClaimData claims) {
        String seed = claims.getSeed();

        if (seed == null || seed.isEmpty()) {
            log.warn("Seed está vazia ou nula");
            return ValidationResult.failure(ErrorMessages.SEED_EMPTY);
        }

        if (!PrimeNumberValidator.isPrime(seed)) {
            log.warn("Seed não é um número primo: {}", seed);
            return ValidationResult.failure(ErrorMessages.SEED_NOT_PRIME);
        }

        log.debug("Validação da seed passou: {}", seed);
        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 4;
    }
}
