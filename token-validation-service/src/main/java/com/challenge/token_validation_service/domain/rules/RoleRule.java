package com.challenge.token_validation_service.domain.rules;

import static com.challenge.token_validation_service.shared.constants.ErrorMessages.ROLE_EMPTY;
import static com.challenge.token_validation_service.shared.constants.ErrorMessages.ROLE_INVALID;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoleRule implements ValidationRule {

  private static final Set<String> VALID = Set.of("Admin", "Member", "External");

  @Override
  public ValidationResult validate(ClaimData claims) {

    String role = claims.getRole();

    if (role == null || role.isBlank()) {

      log.warn("Validation failed. rule=RoleRule, reason=Role is empty");

      return ValidationResult.failure(ROLE_EMPTY);
    }

    if (!VALID.contains(role)) {

      log.warn("Validation failed. rule=RoleRule, reason=Invalid role");

      return ValidationResult.failure(ROLE_INVALID);
    }

    return ValidationResult.success();
  }

  @Override
  public int getOrder() {
    return 3;
  }
}
