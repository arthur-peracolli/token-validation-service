package com.challenge.token_validation_service.infrastructure.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.challenge.token_validation_service.domain.services.TokenValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class TokenValidationHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final AtomicReference<TokenValidationService> serviceRef = new AtomicReference<>();
    private static final AtomicReference<Exception> initError = new AtomicReference<>();

    private TokenValidationService getTokenValidationService() {
        if (serviceRef.get() == null && initError.get() == null) {
            synchronized (TokenValidationHandler.class) {
                if (serviceRef.get() == null && initError.get() == null) {
                    try {
                        log.info("Inicializando TokenValidationService...");
                        
                        var jwtParserService = new com.challenge.token_validation_service.domain.services.JwtParserService();
                        var rules = java.util.List.of(
                                new com.challenge.token_validation_service.domain.rules.ClaimCountRule(),
                                new com.challenge.token_validation_service.domain.rules.NameRule(),
                                new com.challenge.token_validation_service.domain.rules.RoleRule(),
                                new com.challenge.token_validation_service.domain.rules.SeedRule()
                        );
                        
                        TokenValidationService service = new TokenValidationService(jwtParserService, rules);
                        serviceRef.set(service);
                        log.info("TokenValidationService inicializado com sucesso!");
                        
                    } catch (Exception e) {
                        log.error("Erro ao inicializar TokenValidationService: {}", e.getMessage(), e);
                        initError.set(e);
                        throw new RuntimeException("Falha ao inicializar serviço", e);
                    }
                }
            }
        }
        
        if (initError.get() != null) {
            throw new RuntimeException("Serviço falhou na inicialização", initError.get());
        }
        
        return serviceRef.get();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        log.info("Recebida requisição Lambda para validação de token");

        try {
            String token = extractTokenFromQueryString(event);

            if (token == null || token.isEmpty()) {
                log.warn("Token não fornecido na requisição");
                return createErrorResponse(400, "Token is required");
            }

            log.debug("Token extraído com sucesso, iniciando validação");
            boolean isValid = getTokenValidationService().validateToken(token);

            log.info("Validação concluída. Token válido: {}", isValid);
            return createSuccessResponse(isValid);

        } catch (Exception e) {
            log.error("Erro ao processar requisição: {}", e.getMessage(), e);
            return createErrorResponse(500, "Internal server error: " + e.getMessage());
        }
    }

    private String extractTokenFromQueryString(APIGatewayProxyRequestEvent event) {
        Map<String, String> queryStringParams = event.getQueryStringParameters();
        if (queryStringParams == null) {
            return null;
        }
        String token = queryStringParams.get("token");
        if (token == null) {
            token = queryStringParams.get("Token");
        }
        return token;
    }

    private APIGatewayProxyResponseEvent createSuccessResponse(boolean isValid) {
        try {
            String body = objectMapper.writeValueAsString(Map.of("valid", isValid));
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(200);
            response.setBody(body);
            response.setHeaders(Map.of("Content-Type", "application/json"));
            return response;
        } catch (Exception e) {
            log.error("Erro ao serializar resposta", e);
            return createErrorResponse(500, "Error processing response");
        }
    }

    private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
        try {
            String body = objectMapper.writeValueAsString(Map.of(
                    "error", message,
                    "statusCode", statusCode
            ));
            APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
            response.setStatusCode(statusCode);
            response.setBody(body);
            response.setHeaders(Map.of("Content-Type", "application/json"));
            return response;
        } catch (Exception e) {
            APIGatewayProxyResponseEvent fallback = new APIGatewayProxyResponseEvent();
            fallback.setStatusCode(statusCode);
            fallback.setBody("{\"error\":\"Internal server error\"}");
            return fallback;
        }
    }
}