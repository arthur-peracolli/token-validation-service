package com.challenge.token_validation_service.domain.rules;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;

public interface ValidationRule{
    ValidationResult validate(ClaimData claims);

    default int getOrder() { return 0; }
}