package com.challenge.token_validation_service.integration;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.token_validation_service.application.dtos.ValidationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TokenValidationIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setup() {

        restClient =
                RestClient.builder()
                        .baseUrl(
                                "http://localhost:" + port
                        )
                        .build();
    }

    @Test
    @DisplayName("Case 1 - Valid token should return true")
    void shouldReturnTrueForValidToken() {

        ValidationResponse response =
                validate(
                        "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"
                );

        assertNotNull(response);
        assertTrue(response.isValid());
    }

    @Test
    @DisplayName("Case 2 - Invalid JWT should return false")
    void shouldReturnFalseForInvalidJwt() {

        ValidationResponse response =
                validate(
                        "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg"
                );

        assertNotNull(response);
        assertFalse(response.isValid());
    }

    @Test
    @DisplayName("Case 3 - Name containing numbers should return false")
    void shouldReturnFalseForInvalidName() {

        ValidationResponse response =
                validate(
                        "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs"
                );

        assertNotNull(response);
        assertFalse(response.isValid());
    }

    @Test
    @DisplayName("Case 4 - JWT with more than 3 claims should return false")
    void shouldReturnFalseForMoreThanThreeClaims() {

        ValidationResponse response =
                validate(
                        "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY"
                );

        assertNotNull(response);
        assertFalse(response.isValid());
    }

    private ValidationResponse validate(String token) {

        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/validate")
                                .queryParam("token", token)
                                .build()
                )
                .retrieve()
                .body(ValidationResponse.class);
    }
}