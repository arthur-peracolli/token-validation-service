package com.challenge.token_validation_service.domain.rules;

import static com.challenge.token_validation_service.shared.constants.ErrorMessages.*;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NameRule implements ValidationRule {

  private static final String NUMBER_REGEX = ".*\\d.*";

  @Override
  public ValidationResult validate(ClaimData claim) {

    String name = claim.getName();

    if (name == null || name.isBlank()) {
      log.warn("Validation failed. rule=NameRule, reason=Name is empty");
      return ValidationResult.failure(NAME_EMPTY);
    }

    if (name.length() > 256) {
      log.warn("Validation failed. rule=NameRule, reason=Name exceeds max length");
      return ValidationResult.failure(NAME_TOO_LONG);
    }

    if (name.matches(NUMBER_REGEX)) {
      log.warn("Validation failed. rule=NameRule, reason=Name contains numbers");
      return ValidationResult.failure(NAME_CONTAINS_NUMBER);
    }

    return ValidationResult.success();
  }

  @Override
  public int getOrder() {
    return 2;
  }
}
