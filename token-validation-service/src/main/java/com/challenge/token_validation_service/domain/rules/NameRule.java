package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.shared.constants.ErrorMessages;
import com.challenge.token_validation_service.domain.models.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NameRule implements ValidationRule {

    private static final String NUMBER_REGEX = ".*[0-9].*";

    @Override
    public ValidationResult validate(Claims claims) {
        String name = claims.getName();

        if (name == null || name.isEmpty()) {
            log.warn("Nome está vazio ou nulo");
            return ValidationResult.failure(ErrorMessages.NAME_EMPTY);
        }

        if (name.length() > 256) {
            log.warn("Nome excede 256 caracteres: {}", name.length());
            return ValidationResult.failure(ErrorMessages.NAME_TOO_LONG);
        }

        if (name.matches(NUMBER_REGEX)) {
            log.warn("Nome contém números: {}", name);
            return ValidationResult.failure(ErrorMessages.NAME_CONTAINS_NUMBER);
        }

        log.debug("Validação do nome passou: {}", name);
        return ValidationResult.success();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
