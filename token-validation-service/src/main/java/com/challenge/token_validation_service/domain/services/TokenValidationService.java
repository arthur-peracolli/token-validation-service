package com.challenge.token_validation_service.domain.services;

import com.challenge.token_validation_service.domain.models.ClaimData;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.domain.rules.ValidationRule;
import com.challenge.token_validation_service.infrastructure.jwt.JwtPayloadExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private final JwtPayloadExtractor extractor;
    private final List<ValidationRule> rules;

    public boolean validateToken(String token) {

        try {

            ClaimData claims =
                    extractor.extract(token);

            for (ValidationRule rule : orderedRules()) {

                ValidationResult result =
                        rule.validate(claims);

                if (!result.isValid()) {
                    return false;
                }
            }

            return true;

        } catch (Exception ex) {

            return false;
        }
    }

    private List<ValidationRule> orderedRules() {
        return rules.stream()
                .sorted(Comparator.comparingInt(
                        ValidationRule::getOrder))
                .toList();
    }
}