package com.challenge.token_validation_service.domain.services;

import com.challenge.token_validation_service.domain.models.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JwtParserService {

    private static final int EXPECTED_CLAIM_COUNT = 3;

    public Claims parseClaims(String token) throws JwtException {
        log.debug("Iniciando decodificação do token JWT (estrutura apenas, sem validação de assinatura)");

        if (token == null || token.isEmpty()) {
            log.warn("Token vazio ou nulo recebido");
            throw new JwtException("Token vazio ou nulo");
        }

        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                log.warn("Token com estrutura inválida: contém {} partes, esperado: 2 ou 3", parts.length);
                throw new JwtException("Token inválido: deve ter pelo menos 2 partes separadas por ponto (.)");
            }

            String encodedPayload = parts[1];

            String decodedPayload;
            try {
                decodedPayload = new String(Base64.getUrlDecoder().decode(encodedPayload));
                log.debug("Payload decodificado com sucesso do Base64");
            } catch (IllegalArgumentException e) {
                log.warn("Falha ao decodificar payload Base64");
                throw new JwtException("Payload não é um Base64 válido", e);
            }

            int claimCount = countClaimsInJson(decodedPayload);
            if (claimCount != EXPECTED_CLAIM_COUNT) {
                log.warn("Número inválido de claims: esperado {}, encontrado {}", EXPECTED_CLAIM_COUNT, claimCount);
                throw new JwtException(
                        String.format("JWT deve conter exatamente %d claims, encontrado: %d", 
                                EXPECTED_CLAIM_COUNT, claimCount)
                );
            }

            String name = extractClaim(decodedPayload, "Name");
            String role = extractClaim(decodedPayload, "Role");
            String seed = extractClaim(decodedPayload, "Seed");

            if (name == null || role == null || seed == null) {
                log.warn("Claims obrigatórias ausentes no token. Name={}, Role={}, Seed={}", 
                        name != null, role != null, seed != null);
                throw new JwtException("JWT não contém as claims obrigatórias: Name, Role, Seed");
            }

            log.debug("Token decodificado com sucesso. Claims extraídas: Name='{}', Role='{}', Seed='{}'", 
                    name, role, seed);

            return Claims.builder()
                    .name(name)
                    .role(role)
                    .seed(seed)
                    .build();

        } catch (JwtException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Erro inesperado ao decodificar token: {}", e.getMessage());
            throw new JwtException("Token inválido ou malformado", e);
        }
    }

    private int countClaimsInJson(String json) {
        json = json.trim();

        if (!json.startsWith("{") || !json.endsWith("}")) {
            log.debug("JSON não é um object válido (deve iniciar com {{ e terminar com }})");
            return 0;
        }

        Pattern pattern = Pattern.compile("\"[^\"]+\"\\s*:");
        int count = 0;
        var matcher = pattern.matcher(json);
        while (matcher.find()) {
            count++;
        }
        
        log.debug("Contagem de claims no JSON: {}", count);
        return count;
    }

    private String extractClaim(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*)\"";
        Pattern p = Pattern.compile(pattern);
        var m = p.matcher(json);
        
        if (m.find()) {
            String value = m.group(1);
            log.debug("Claim '{}' extraída com valor: '{}'", key, value);
            return value;
        }
        
        log.debug("Claim '{}' não encontrada no JSON", key);
        return null;
    }
}