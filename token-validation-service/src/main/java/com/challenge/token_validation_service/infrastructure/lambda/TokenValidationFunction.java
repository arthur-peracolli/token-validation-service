package com.challenge.token_validation_service.infrastructure.lambda;

import com.challenge.token_validation_service.domain.services.TokenValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidationFunction implements Function<Map<String, Object>, Map<String, Object>> {

    private final TokenValidationService tokenValidationService;

    @Override
    public Map<String, Object> apply(Map<String, Object> input) {
        log.info("Recebida requisição de validação de token");

        try {
            String token = extractToken(input);

            if (token == null || token.isEmpty()) {
                log.warn("Token não fornecido na requisição");
                return Map.of(
                        "statusCode", 400,
                        "body", "{\"error\":\"Token is required\"}"
                );
            }

            log.debug("Token extraído com sucesso, iniciando validação");
            boolean isValid = tokenValidationService.validateToken(token);

            log.info("Validação concluída. Token válido: {}", isValid);
            String body = String.format("{\"valid\":%b}", isValid);
            
            return Map.of(
                    "statusCode", 200,
                    "body", body,
                    "headers", Map.of("Content-Type", "application/json")
            );

        } catch (Exception e) {
            log.error("Erro ao processar requisição: {}", e.getMessage(), e);
            String errorBody = String.format("{\"error\":\"Internal server error: %s\"}", e.getMessage());
            return Map.of(
                    "statusCode", 500,
                    "body", errorBody,
                    "headers", Map.of("Content-Type", "application/json")
            );
        }
    }

    private String extractToken(Map<String, Object> input) {
        try {
            // Verifica se é um evento do API Gateway
            Map<String, Object> queryParams = null;
            
            if (input.containsKey("queryStringParameters")) {
                Object qsp = input.get("queryStringParameters");
                if (qsp instanceof Map) {
                    queryParams = (Map<String, Object>) qsp;
                }
            }

            if (queryParams != null) {
                // Tenta buscar o token com diferentes variações de case
                if (queryParams.containsKey("token")) {
                    return (String) queryParams.get("token");
                }
                if (queryParams.containsKey("Token")) {
                    return (String) queryParams.get("Token");
                }
            }
        } catch (Exception e) {
            log.warn("Erro ao extrair token dos query params: {}", e.getMessage());
        }

        return null;
    }
}

