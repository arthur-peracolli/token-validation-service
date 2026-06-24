package com.challenge.token_validation_service.infrastructure.jwt;

import com.challenge.token_validation_service.domain.models.ClaimData;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtPayloadExtractor {

    private final ObjectMapper mapper;

    public ClaimData extract(String token) {

        String[] parts = token.split("\\.");

        if (parts.length != 3) {
            throw new JwtException("Invalid JWT structure");
        }

        String payload =
                new String(Base64.getUrlDecoder().decode(parts[1]));

        JsonNode root = mapper.readTree(payload);

        return ClaimData.builder()
                .name(root.path("Name").asText(null))
                .role(root.path("Role").asText(null))
                .seed(root.path("Seed").asText(null))
                .claimCount(root.size())
                .build();
    }
}