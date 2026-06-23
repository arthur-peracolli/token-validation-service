package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.Claims;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.shared.constants.ErrorMessages;
import com.challenge.token_validation_service.shared.constants.Roles;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RoleRule implements ValidationRule {

    private static final Set<String> VALID_ROLES =
            Arrays.stream(Roles.VALID_ROLES).collect(Collectors.toSet());

    @Override
    public ValidationResult validate(Claims claims) {
        String role = claims.getRole();

        if (role == null || role.isEmpty()) {
            log.warn("Role está vazia ou nula");
            return ValidationResult.failure(ErrorMessages.ROLE_EMPTY);
        }

        if (!VALID_ROLES.contains(role)) {
            log.warn("Role inválida: {} (esperado: {})", role, Roles.VALID_ROLES);
            return ValidationResult.failure(
                    String.format(ErrorMessages.ROLE_INVALID, role, String.join(", ", Roles.VALID_ROLES))
            );
        }

        log.debug("Validação da role passou: {}", role);
        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 3;
    }
}