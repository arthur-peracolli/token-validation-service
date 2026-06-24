package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class NameRule implements ValidationRule {

    private static final String NUMBER_REGEX = ".*\\d.*";

    @Override
    public ValidationResult validate(ClaimData claim) {

        String name = claim.getName();

        if (name == null || name.isBlank()) {
            return ValidationResult.failure("Name is empty");
        }

        if (name.length() > 256) {
            return ValidationResult.failure("Name too long");
        }

        if (name.matches(NUMBER_REGEX)) {
            return ValidationResult.failure("Name contains numbers");
        }

        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
