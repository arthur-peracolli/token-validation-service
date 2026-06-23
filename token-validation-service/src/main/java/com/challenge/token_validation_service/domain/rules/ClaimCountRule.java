package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.Claims;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.shared.constants.ErrorMessages;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClaimCountRule implements ValidationRule {

    private static final int EXPECTED_CLAIM_COUNT = 3;

    @Override
    public ValidationResult validate(Claims claims) {
        if (claims == null) {
            log.warn("Claims estão nulas");
            return ValidationResult.failure(ErrorMessages.CLAIMS_NULL);
        }

        long claimCount = claims.countFilledClaims();

        if (claimCount != EXPECTED_CLAIM_COUNT) {
            log.warn("Número de claims inválido: esperado {}, encontrado {}",
                    EXPECTED_CLAIM_COUNT, claimCount);
            return ValidationResult.failure(
                    String.format(ErrorMessages.CLAIM_COUNT_INVALID, EXPECTED_CLAIM_COUNT, claimCount)
            );
        }

        log.debug("Validação de quantidade de claims passou: {}", claimCount);
        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}