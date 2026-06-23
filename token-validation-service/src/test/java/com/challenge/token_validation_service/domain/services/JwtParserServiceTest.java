package com.challenge.token_validation_service.domain.services;

import com.challenge.token_validation_service.domain.models.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("JwtParserService Tests")
class JwtParserServiceTest {

    private JwtParserService jwtParserService;

    @BeforeEach
    void setUp() {
        jwtParserService = new JwtParserService();
    }

    @Nested
    @DisplayName("Token Válido - Casos de Sucesso (Parsing Estrutural)")
    class ValidTokenTests {

        @Test
        @DisplayName("Deve parsear token com Role=Admin e Seed primo (estrutura válida)")
        void testParseValidTokenWithRoleAdminAndPrimeSeed() {
            String payload = "{\"Name\":\"John Doe\",\"Role\":\"Admin\",\"Seed\":\"13\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("John Doe", claims.getName());
            assertEquals("Admin", claims.getRole());
            assertEquals("13", claims.getSeed());
        }

        @Test
        @DisplayName("Deve parsear token com Role=Member e Seed primo (estrutura válida)")
        void testParseValidTokenWithRoleMemberAndPrimeSeed() {
            String payload = "{\"Name\":\"Jane Smith\",\"Role\":\"Member\",\"Seed\":\"17\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("Jane Smith", claims.getName());
            assertEquals("Member", claims.getRole());
            assertEquals("17", claims.getSeed());
        }

        @Test
        @DisplayName("Deve parsear token com Role=External e Seed primo (estrutura válida)")
        void testParseValidTokenWithRoleExternalAndPrimeSeed() {
            String payload = "{\"Name\":\"Bob Johnson\",\"Role\":\"External\",\"Seed\":\"19\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("Bob Johnson", claims.getName());
            assertEquals("External", claims.getRole());
            assertEquals("19", claims.getSeed());
        }

        @Test
        @DisplayName("Deve parsear token com Name contendo apenas letras e espaços (estrutura válida)")
        void testParseValidTokenWithNameHavingOnlyLettersAndSpaces() {
            String payload = "{\"Name\":\"Maria da Silva\",\"Role\":\"Admin\",\"Seed\":\"23\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("Maria da Silva", claims.getName());
        }

        @Test
        @DisplayName("Deve parsear token com Name de 256 caracteres (estrutura válida)")
        void testParseValidTokenWithMaxLengthName() {
            String maxName = "A".repeat(256);
            String payload = "{\"Name\":\"" + maxName + "\",\"Role\":\"Member\",\"Seed\":\"29\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals(maxName, claims.getName());
            assertEquals(256, claims.getName().length());
        }

        @Test
        @DisplayName("Deve parsear token com ordem diferente de claims (estrutura válida)")
        void testParseValidTokenWithDifferentClaimOrder() {
            String payload = "{\"Role\":\"External\",\"Seed\":\"31\",\"Name\":\"Test User\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("Test User", claims.getName());
            assertEquals("External", claims.getRole());
            assertEquals("31", claims.getSeed());
        }

        @Test
        @DisplayName("Deve parsear token com Name contendo caracteres acentuados (estrutura válida)")
        void testParseValidTokenWithAccentedCharactersInName() {
            String payload = "{\"Name\":\"José María García\",\"Role\":\"Admin\",\"Seed\":\"37\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);

            assertNotNull(claims);
            assertEquals("José María García", claims.getName());
        }
    }

    @Nested
    @DisplayName("Token Inválido - Formato Estrutural")
    class InvalidTokenFormatTests {

        @Test
        @DisplayName("Deve lançar exceção quando token é nulo")
        void testThrowExceptionWhenTokenIsNull() {
            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(null);
            });

            assertTrue(exception.getMessage().contains("vazio ou nulo"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando token é vazio")
        void testThrowExceptionWhenTokenIsEmpty() {
            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims("");
            });

            assertTrue(exception.getMessage().contains("vazio ou nulo"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando token tem apenas 1 parte")
        void testThrowExceptionWhenTokenHasOnlyOnePart() {
            String token = "header";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("deve ter pelo menos 2 partes"));
        }
    }

    @Nested
    @DisplayName("Token Inválido - Contagem de Claims")
    class InvalidClaimCountTests {

        @Test
        @DisplayName("Deve lançar exceção quando token tem 2 claims ao invés de 3")
        void testThrowExceptionWhenTokenHasTwoClaims() {
            String payload = "{\"Name\":\"John\",\"Role\":\"Admin\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("exatamente 3 claims"));
            assertTrue(exception.getMessage().contains("encontrado: 2"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando token tem 4 claims ao invés de 3")
        void testThrowExceptionWhenTokenHasFourClaims() {
            String payload = "{\"Name\":\"John\",\"Role\":\"Admin\",\"Seed\":\"13\",\"Extra\":\"claim\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("exatamente 3 claims"));
            assertTrue(exception.getMessage().contains("encontrado: 4"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando token tem 0 claims")
        void testThrowExceptionWhenTokenHasZeroClaims() {
            String payload = "{}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("exatamente 3 claims"));
            assertTrue(exception.getMessage().contains("encontrado: 0"));
        }
    }

    @Nested
    @DisplayName("Token Inválido - Claims Obrigatórias Faltando")
    class MissingRequiredClaimsTests {

        @Test
        @DisplayName("Deve lançar exceção quando claim 'Name' está faltando")
        void testThrowExceptionWhenNameClaimIsMissing() {
            String payload = "{\"Role\":\"Admin\",\"Seed\":\"13\",\"Extra\":\"field\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("Name, Role, Seed"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando claim 'Role' está faltando")
        void testThrowExceptionWhenRoleClaimIsMissing() {
            String payload = "{\"Name\":\"John\",\"Seed\":\"13\",\"Extra\":\"field\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("Name, Role, Seed"));
        }

        @Test
        @DisplayName("Deve lançar exceção quando claim 'Seed' está faltando")
        void testThrowExceptionWhenSeedClaimIsMissing() {
            String payload = "{\"Name\":\"John\",\"Role\":\"Admin\",\"Extra\":\"field\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            JwtException exception = assertThrows(JwtException.class, () -> {
                jwtParserService.parseClaims(token);
            });

            assertTrue(exception.getMessage().contains("Name, Role, Seed"));
        }
    }

    @Nested
    @DisplayName("Token Válido - Casos de Negócio (validados pela TokenValidationService)")
    class ValidBusinessLogicTests {

        @Test
        @DisplayName("Parser aceita Name com números (validação de negócio delegada)")
        void testParserAcceptsNameWithNumbers() {
            String payload = "{\"Name\":\"John123\",\"Role\":\"Admin\",\"Seed\":\"13\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);
            assertNotNull(claims);
            assertEquals("John123", claims.getName());
        }

        @Test
        @DisplayName("Parser aceita Role inválida (validação de negócio delegada)")
        void testParserAcceptsInvalidRole() {
            String payload = "{\"Name\":\"John Doe\",\"Role\":\"SuperAdmin\",\"Seed\":\"13\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);
            assertNotNull(claims);
            assertEquals("SuperAdmin", claims.getRole());
        }

        @Test
        @DisplayName("Parser aceita Seed não primo (validação de negócio delegada)")
        void testParserAcceptsNonPrimeSeed() {
            String payload = "{\"Name\":\"John Doe\",\"Role\":\"Admin\",\"Seed\":\"4\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);
            assertNotNull(claims);
            assertEquals("4", claims.getSeed());
        }

        @Test
        @DisplayName("Parser aceita Name > 256 caracteres (validação de negócio delegada)")
        void testParserAcceptsLongName() {
            String longName = "A".repeat(300);
            String payload = "{\"Name\":\"" + longName + "\",\"Role\":\"Admin\",\"Seed\":\"13\"}";
            String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());
            String token = "header." + encodedPayload + ".signature";

            Claims claims = jwtParserService.parseClaims(token);
            assertNotNull(claims);
            assertEquals(longName, claims.getName());
            assertEquals(300, claims.getName().length());
        }
    }
}