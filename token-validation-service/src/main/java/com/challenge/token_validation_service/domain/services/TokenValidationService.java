package com.challenge.token_validation_service.domain.services;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.domain.rules.ValidationRule;
import com.challenge.token_validation_service.infrastructure.jwt.JwtPayloadExtractor;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenValidationService {

  private final JwtPayloadExtractor extractor;
  private final List<ValidationRule> rules;

  public TokenValidationService(JwtPayloadExtractor extractor, List<ValidationRule> rules) {

    this.extractor = extractor;
    this.rules = List.copyOf(rules);
  }

  public boolean validateToken(String token) {

    log.info("Token validation started");

    try {

      ClaimData claims = extractor.extract(token);

      for (ValidationRule rule : orderedRules()) {

        ValidationResult result = rule.validate(claims);

        if (!result.isValid()) {

          log.info(
              "Token validation completed. valid=false, failedRule={}",
              rule.getClass().getSimpleName());

          return false;
        }
      }

      log.info("Token validation completed. valid=true");

      return true;

    } catch (Exception ex) {

      log.error("Unexpected error during token validation", ex);

      return false;
    }
  }

  private List<ValidationRule> orderedRules() {

    return rules.stream().sorted(Comparator.comparingInt(ValidationRule::getOrder)).toList();
  }
}
