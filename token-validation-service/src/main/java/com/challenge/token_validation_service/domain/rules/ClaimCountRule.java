package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class ClaimCountRule implements ValidationRule {

    @Override
    public ValidationResult validate(ClaimData claim) {

        return claim.getClaimCount() == 3
                ? ValidationResult.success()
                : ValidationResult.failure("JWT must contain exactly 3 claims");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}