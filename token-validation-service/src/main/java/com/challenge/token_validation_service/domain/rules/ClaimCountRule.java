package com.challenge.token_validation_service.domain.rules;

import static com.challenge.token_validation_service.shared.constants.ErrorMessages.CLAIM_COUNT_INVALID;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClaimCountRule implements ValidationRule {

  @Override
  public ValidationResult validate(ClaimData claim) {

    if (claim.getClaimCount() != 3) {

      log.warn("Validation failed. rule=ClaimCountRule, reason=Invalid claim count");

      return ValidationResult.failure(CLAIM_COUNT_INVALID);
    }

    return ValidationResult.success();
  }

  @Override
  public int getOrder() {
    return 1;
  }
}
