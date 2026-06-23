package com.challenge.token_validation_service.domain.services;

import com.challenge.token_validation_service.domain.models.Claims;
import com.challenge.token_validation_service.domain.models.ValidationResult;
import com.challenge.token_validation_service.domain.rules.ValidationRule;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private final JwtParserService jwtParserService;
    private final List<ValidationRule> rules;

    public boolean validateToken(String token) {
        log.info("Iniciando validação do token JWT");

        if (token == null || token.isEmpty()) {
            log.warn("Validação falhou: token é nulo ou vazio");
            return false;
        }

        try {
            log.debug("Etapa 1: Decodificando token JWT...");
            Claims claims = jwtParserService.parseClaims(token);
            log.debug("Token decodificado com sucesso. Claims: Name={}, Role={}, Seed={}",
                    claims.getName(), claims.getRole(), claims.getSeed());


            log.debug("Etapa 2: Executando {} regras de validação de negócio", rules.size());
            List<ValidationRule> sortedRules = rules.stream()
                    .sorted(Comparator.comparingInt(ValidationRule::getOrder))
                    .toList();

            for (ValidationRule rule : sortedRules) {
                String ruleName = rule.getClass().getSimpleName();
                log.debug("Executando regra: {} (ordem {})", ruleName, rule.getOrder());

                ValidationResult result = rule.validate(claims);

                if (!result.isValid()) {
                    log.warn("Validação falhou na regra '{}': {}", ruleName, result.getErrorMessage());
                    return false;
                }
                log.debug("Regra '{}' passou com sucesso", ruleName);
            }

            log.info("✅ Token validado com sucesso! Todas as validações passaram.");
            return true;

        } catch (JwtException e) {
            log.warn("Validação falhou: erro ao decodificar token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Erro inesperado ao validar token", e);
            return false;
        }
    }
}