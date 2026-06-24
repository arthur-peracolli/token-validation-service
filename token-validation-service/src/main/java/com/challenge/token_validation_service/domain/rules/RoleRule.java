package com.challenge.token_validation_service.domain.rules;


import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoleRule implements ValidationRule {

    private static final Set<String> VALID =
            Set.of("Admin", "Member", "External");

    @Override
    public ValidationResult validate(ClaimData claims) {

        String role = claims.getRole();

        if (role == null || role.isBlank()) {
            return ValidationResult.failure("Role is empty");
        }

        if (!VALID.contains(role)) {
            return ValidationResult.failure("Invalid role");
        }

        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}