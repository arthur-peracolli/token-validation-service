package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.Claims;
import com.challenge.token_validation_service.domain.models.ValidationResult;

public interface ValidationRule{
    ValidationResult validate(Claims claims);

    default int getOrder() { return 0; }
}